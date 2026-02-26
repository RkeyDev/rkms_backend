package com.rkey.rkms_backend.modules.auth.dto;

import com.rkey.rkms_backend.modules.auth.validation.PasswordMatch;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@PasswordMatch(
    passwordField = "password", 
    confirmPasswordField = "confirmPassword", 
    message = "Passwords must match"
)

//DTO to send login request to server
public record UserLoginDTO(
    @NotBlank(message = "Email must be filled")
    @Email(message = "Must be a valid email structure")
    String email,

    @NotBlank(message = "Password must be filled")
    @Size(min=8, message="Password must be at least 8 characters long")
    String password
) 
{}
