package com.rkey.rkms_backend.modules.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rkey.rkms_backend.core.api.ApiResponse;
import com.rkey.rkms_backend.core.api.ResponseType;
import com.rkey.rkms_backend.modules.auth.dto.UnlockAccountDTO;
import com.rkey.rkms_backend.modules.auth.dto.UserLoggedInDTO;
import com.rkey.rkms_backend.modules.auth.dto.UserLoginDTO;
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
    public ResponseEntity<ApiResponse<Void>> handleUserRegistration(@Valid @RequestBody UserRegistrationDTO request){
        authService.registerUser(request);

        return ApiResponse.toResponseEntity(ResponseType.ACCOUNT_CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoggedInDTO>> handleUserLogin(@Valid @RequestBody UserLoginDTO request) {
        return authService.loginUser(request)
        .map(dto -> ApiResponse.toResponseEntity(ResponseType.LOGIN_SUCCESS,dto))
        .orElseGet(() -> ApiResponse.toResponseEntity(ResponseType.UNAUTHORIZED));
    }
    
    @PostMapping("/unlock-account")
    public ResponseEntity<ApiResponse<UserLoggedInDTO>> handleUnlockAccount(@Valid @RequestBody UnlockAccountDTO request) {
        
        return authService.unlockAccount(request)
        .map(dto -> ApiResponse.toResponseEntity(ResponseType.LOGIN_SUCCESS,dto))
        .orElseThrow(() -> new RuntimeException("User was not found."))
        ;

    }
    
 
}