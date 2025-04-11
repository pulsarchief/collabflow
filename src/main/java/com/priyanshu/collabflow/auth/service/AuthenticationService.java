package com.priyanshu.collabflow.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.priyanshu.collabflow.auth.dto.AuthenticationRequest;
import com.priyanshu.collabflow.auth.dto.AuthenticationResponse;
import com.priyanshu.collabflow.auth.dto.RegisterRequest;
import com.priyanshu.collabflow.auth.jwt.JwtService;
import com.priyanshu.collabflow.user.entity.Role;
import com.priyanshu.collabflow.user.entity.User;
import com.priyanshu.collabflow.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.var;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder().fullName(request.getFullName()).email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() == null ? Role.USER : request.getRole()).build();
        userRepository.save(user);

        String jwToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

}
