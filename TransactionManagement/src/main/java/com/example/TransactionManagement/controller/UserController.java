package com.example.TransactionManagement.controller;


import com.example.TransactionManagement.User;
import com.example.TransactionManagement.exceptions.UserAlreadyEnrolledException;
import com.example.TransactionManagement.exceptions.UserNotFoundException;
import com.example.TransactionManagement.service.AuthenticationService;
import com.example.TransactionManagement.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    AuthenticationService service;

//    @PostMapping("/register")
//    public ResponseEntity<User> registerUser(@RequestParam int user_id, @RequestParam String user_name, @RequestParam String address, @RequestParam Double latitude, @RequestParam Double longitude) {
//        try {
//            User user = userService.registerUser(user_id, user_name, address, latitude, longitude);
//            return ResponseEntity.ok(user);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @PostMapping("/enroll")
    public ResponseEntity<String> enrollForOfflinePayment(@RequestParam int user_id) {
        try {
            userService.enrollForOfflinePayment(user_id);
            return ResponseEntity.ok("Enrolled for offline payment successfully.");
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (UserAlreadyEnrolledException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is already enrolled.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }
}
