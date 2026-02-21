package com.rkey.rkms_backend.modules.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rkey.rkms_backend.modules.auth.dto.UserRegistrationDTO;
import com.rkey.rkms_backend.modules.auth.service.AuthService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService; 
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> handleUserRegistration(@Valid @RequestBody UserRegistrationDTO request){
        authService.registerUser(request);

        return new ResponseEntity<>("Locked account created",HttpStatus.CREATED);
    }
 
}