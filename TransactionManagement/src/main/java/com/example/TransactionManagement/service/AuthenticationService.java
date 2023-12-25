package com.example.TransactionManagement.service;


import com.example.TransactionManagement.User;
import com.example.TransactionManagement.Role;

import com.example.TransactionManagement.config.JwtService;
import com.example.TransactionManagement.controller.AuthenticationRequest;
import com.example.TransactionManagement.controller.AuthenticationResponse;
import com.example.TransactionManagement.controller.RegisterRequest;
import com.example.TransactionManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    UserRepository repository;

    @Autowired
    JwtService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = new User();
        user.setUser_id(request.getUser_id());
        user.setName(request.getUser_name());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUser_secret(generateUserSecret());
        user.setUser_status(true);
        user.setUser_enrolled(false);
        user.setUser_enrollapproved(false);
        user.setUser_latitude(request.getLatitude());
        user.setUser_longitude(request.getLongitude());
        user.setRole(Role.USER);
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtToken);
        return authenticationResponse;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUser_name(), request.getPassword()));
        User user = repository.findByName(request.getUser_name()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtToken);
        return authenticationResponse;
    }

    public String generateUserSecret(){
        return UUID.randomUUID().toString();
    }

}
