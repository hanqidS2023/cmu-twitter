package com.cmux.user.service;

import com.cmux.user.dto.JwtResponse;
import com.cmux.user.dto.LoginRequest;
import com.cmux.user.dto.RefreshTokenRequest;
import com.cmux.user.dto.SignUpRequest;
import com.cmux.user.dto.UserUpdateRequest;
import com.cmux.user.dto.UserDTO;
import com.cmux.user.entity.User;
import com.cmux.user.repository.UserRepository;
import com.cmux.user.security.CustomUserDetails;
import com.cmux.user.utils.AccessTokenFactory;
import com.cmux.user.utils.JwtUtils;
import com.cmux.user.utils.RefreshTokenFactory;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private AccessTokenFactory accessTokenFactory;

    @Autowired
    private RefreshTokenFactory refreshTokenFactory;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CacheManager cacheManager;

    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setUserImage(user.getUserImage());
        userDTO.setUnlockedImages(user.getUnlockedImages());
        userDTO.setUnlockedImageIds(user.getUnlockedImageIds());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setBio(user.getBio());
        return userDTO;
    }

    public User registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new DataIntegrityViolationException("Error: Username is already taken");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DataIntegrityViolationException("Error: Email is already in use");
        }

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        return userRepository.save(user);
    }

    public JwtResponse loginUser(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
            String accessToken = accessTokenFactory.createToken(userPrincipal);
            String refreshToken = refreshTokenFactory.createToken(userPrincipal);
            return new JwtResponse(accessToken, refreshToken, userPrincipal.getUserId(), loginRequest.getUsername());
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password", e);
        }
    }

    public JwtResponse refreshAccessToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (!jwtUtils.validateJwtToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        long userId = jwtUtils.getUserIdFromJwtToken(refreshToken);
        String username = jwtUtils.getUserNameFromJwtToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserDetails userDetails = new CustomUserDetails(user);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        String newAccessToken = accessTokenFactory.createToken(customUserDetails);

        return new JwtResponse(newAccessToken, refreshToken, userId, username);
    }

    @Cacheable(value = "users", key = "#id")
    @Transactional
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<User> getUsersByIds(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    @Transactional
    public UserDTO updateUserProfile(Long id, UserUpdateRequest updateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (updateRequest.getUsername() != null) {
            user.setUsername(updateRequest.getUsername());
        }
        if (updateRequest.getEmail() != null) {
            user.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getUserImage() != null) {
            user.setUserImage(updateRequest.getUserImage());
        }
        if (updateRequest.getUnlockedImages() != null) {
            user.setUnlockedImages(updateRequest.getUnlockedImages());
        }
        if (updateRequest.getUnlockedImageIds() != null) {
            user.setUnlockedImageIds(updateRequest.getUnlockedImageIds());
        }
        if(updateRequest.getBio() != null) {
            user.setBio(updateRequest.getBio());
        }
        User updatedUser = userRepository.save(user);
        UserDTO userDTO = convertToDTO(updatedUser);
        Cache usersCache = cacheManager.getCache("users");
        if (usersCache != null) {
            usersCache.put(id, userDTO);
        }
        return userDTO;
    }

    @Transactional
    public void addNewIconToUser(Long userId, String imageUrl, Long productId) {
        UserDTO currentUser = getUserById(userId);
        List<String> imageList = currentUser.getUnlockedImages();
        imageList.add(imageUrl);
        List<Long> imageIdList = currentUser.getUnlockedImageIds();
        imageIdList.add(productId);
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setUnlockedImages(imageList);
        updateRequest.setUnlockedImageIds(imageIdList);
        updateUserProfile(userId, updateRequest);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> searchUsersByKeyword(String keyword) {
        return userRepository.findByUsernameContaining(keyword);
    }

}
