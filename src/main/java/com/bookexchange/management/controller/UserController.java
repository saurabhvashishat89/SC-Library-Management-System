package com.bookexchange.management.controller;


import com.bookexchange.management.entity.User;
import com.bookexchange.management.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registerWithOutName")
    public ResponseEntity<String> registerUser(@RequestHeader String email, @RequestHeader String password) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);

        // Validate email
        if (!pattern.matcher(email).matches()) {
            return ResponseEntity.badRequest().body("Invalid email format. Please provide a valid email.");
        }
        try {
            User user=userService.registerUser(email, password);
            userService.publishMessageToRabbit(user);
            return ResponseEntity.ok("User registered successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
String email = user.getEmail();
String password = user.getPassword();
        // Validate email
        if (!pattern.matcher(email).matches()) {
            return ResponseEntity.badRequest().body("Invalid email format. Please provide a valid email.");
        }
        try {
            User getUser=userService.registerUser(email, password,user);
            userService.publishMessageToRabbit(getUser);
            return ResponseEntity.ok("User registered successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping()
    public List<User> getUsers() throws Exception {

        try {
            List<User> getUser=userService.getUsers();

            return  getUser;
        } catch (RuntimeException e) {
            throw new Exception("exception occurred while fetching the details");
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

