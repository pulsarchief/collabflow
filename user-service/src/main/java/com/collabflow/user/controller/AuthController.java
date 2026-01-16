package com.collabflow.user.controller;

import com.collabflow.common.dto.AuthResponse;
import com.collabflow.common.dto.LoginRequest;
import com.collabflow.common.dto.RegisterRequest;
import com.collabflow.user.model.User;
import com.collabflow.user.repo.UserRepository;
import com.collabflow.user.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (repo.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists.");
        }

        User u = new User();
        u.setEmail(req.getEmail());
        u.setName(req.getName());
        u.setPasswordHash(encoder.encode(req.getPassword()));

        repo.save(u);

        AuthResponse res = new AuthResponse();
        res.setEmail(u.getEmail());
        res.setName(u.getName());
        res.setUserId(u.getId().toString());
        res.setToken(jwtUtil.generateToken(u.getId().toString(), u.getEmail()));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User u = repo.findByEmail(req.getEmail()).orElse(null);

        if (u == null || !encoder.matches(req.getPassword(), u.getPasswordHash())) {
            return ResponseEntity.status(401).body("Invalid credentials.");
        }

        AuthResponse res = new AuthResponse();
        res.setEmail(u.getEmail());
        res.setName(u.getName());
        res.setUserId(u.getId().toString());
        res.setToken(jwtUtil.generateToken(u.getId().toString(), u.getEmail()));

        return ResponseEntity.ok(res);
    }
}
