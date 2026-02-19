package com.rkey.rkms_backend.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationDTO(
    @NotBlank(message="First name must be filled")
    String firstName,

    @NotBlank(message="Last name must be filled")
    String lastName,

    @NotBlank(message = "Email must be filled")
    @Email(message = "Must be a valid email structure")
    String email,

    @NotBlank(message = "Password must be filled")
    @Size(min=8, message="Password must be at least 8 characters long")
    String password,

    @NotBlank(message = "Password must be filled")
    @Size(min=8, message="Password must be at least 8 characters long")
    String confirmPassword

) {}
