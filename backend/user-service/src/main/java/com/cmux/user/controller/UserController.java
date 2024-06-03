package com.cmux.user.controller;

import com.cmux.user.dto.UserDTO;
import com.cmux.user.dto.UserUpdateRequest;
import com.cmux.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cmux.user.entity.User; 
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUsserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        System.out.println("user: " + user);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // Retrieve a list of users by a list of user IDs
    @PostMapping("/users")
    public ResponseEntity<?> getUsersByIds(@RequestBody List<Long> ids) {
        List<User> users = userService.getUsersByIds(ids);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String keyword) {
        List<User> users = userService.searchUsersByKeyword(keyword);
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest updateRequest,
                                    HttpServletRequest request) {
        String headerUserId = request.getHeader("userId");
        if (headerUserId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized update attempt: no userId header!");
        }
        Long userId = Long.parseLong(headerUserId);
        if (!userId.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized update attempt: id mismatch!");
        }
        UserDTO updatedUser = userService.updateUserProfile(id, updateRequest);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUser);
    }
}
