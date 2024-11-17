package com.bookexchange.management.controller;

import com.bookexchange.management.dto.LoginResponseDTO;
import com.bookexchange.management.service.UserService;
import com.bookexchange.management.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestHeader String email, @RequestHeader String password) {
        try {
            LoginResponseDTO loginResponseDTO = userService.authenticateUser(email, password);
            return ResponseEntity.ok(loginResponseDTO);
        } catch (RuntimeException e) {
            throw new RuntimeException("Please enter valid credentials");
//            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        boolean isValid = jwtUtil.validateToken(token);
        return ResponseEntity.ok(isValid);
    }
}

