package com.cmux.user.controller;

import com.cmux.user.dto.JwtResponse;
import com.cmux.user.dto.LoginRequest;
import com.cmux.user.dto.NewUserMessage;
import com.cmux.user.dto.RefreshTokenRequest;
import com.cmux.user.dto.SignUpRequest;
import com.cmux.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cmux.user.entity.User; 
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    MQProducer messageProducer;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = userService.loginUser(loginRequest);
        System.out.println("jwtResponse: " + jwtResponse);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) throws JsonProcessingException {
        User user = userService.registerUser(signUpRequest);
        NewUserMessage newUserMessage = new NewUserMessage(user.getId(), user.getUsername());
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(newUserMessage);
        messageProducer.sendNewUserMessage(jsonString);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        JwtResponse jwtResponse = userService.refreshAccessToken(refreshTokenRequest);
        return ResponseEntity.ok(jwtResponse);
    }


}
