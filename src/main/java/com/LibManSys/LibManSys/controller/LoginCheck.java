package com.LibManSys.LibManSys.controller;

import com.LibManSys.LibManSys.Repository.UserRepository;
import com.LibManSys.LibManSys.dto.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class LoginCheck {

    private final UserRepository userRepository;

    public LoginCheck(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {

        boolean exists = userRepository.userExists(request.getUsername(), request.getPassword());

        if (!exists) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username or password"));
        }

        return ResponseEntity.ok(Map.of("message", "Login successful"));
    }
}
