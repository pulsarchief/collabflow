package com.priyanshu.collabflow.user.dto;

import com.priyanshu.collabflow.user.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String fullName;
    private String email;
    private Role role;
}
