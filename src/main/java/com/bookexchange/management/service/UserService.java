package com.bookexchange.management.service;

import com.bookexchange.management.dto.LoginResponseDTO;
import com.bookexchange.management.entity.User;
import com.bookexchange.management.repository.UserRepository;
import com.bookexchange.management.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
   @Autowired
    JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String email, String password) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already registered!");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public void resetPassword(String email, String newPassword,String oldPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found!"));
       if(!passwordEncoder.matches(oldPassword, user.getPassword()))
       {
          throw new RuntimeException("Password did not match!");
       }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public LoginResponseDTO authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials!"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            String token= jwtUtil.generateToken(email); // Generate JWT
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setToken(token);
            loginResponseDTO.setId(user.getId());
            return loginResponseDTO;
        } else {
            throw new RuntimeException("Invalid credentials!");
        }
    }

}

