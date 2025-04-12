package com.priyanshu.collabflow.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.priyanshu.collabflow.user.dto.UserResponse;
import com.priyanshu.collabflow.user.entity.User;
import com.priyanshu.collabflow.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToDto).toList();
    }

    private UserResponse mapToDto(User user) {
        return UserResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

}
