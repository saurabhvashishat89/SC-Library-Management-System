package com.bookexchange.management.controller;


import com.bookexchange.management.entity.User;
import com.bookexchange.management.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestHeader String email, @RequestHeader String password) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);

        // Validate email
        if (!pattern.matcher(email).matches()) {
            return ResponseEntity.badRequest().body("Invalid email format. Please provide a valid email.");
        }
        try {
            userService.registerUser(email, password);
            return ResponseEntity.ok("User registered successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestHeader String email, @RequestHeader String newPassword, @RequestHeader String oldPassword) {
        try {
            userService.resetPassword(email, newPassword,oldPassword);
            return ResponseEntity.ok("Password reset successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

