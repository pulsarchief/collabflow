package com.collabflow.common.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String userId;
    private String email;
    private String name;
}
