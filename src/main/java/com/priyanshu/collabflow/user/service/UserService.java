package com.priyanshu.collabflow.user.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.priyanshu.collabflow.user.dto.UserResponse;
import com.priyanshu.collabflow.user.entity.User;
import com.priyanshu.collabflow.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getCurrentUserProfile(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
