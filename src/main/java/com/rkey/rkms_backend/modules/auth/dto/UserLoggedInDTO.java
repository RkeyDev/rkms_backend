package com.rkey.rkms_backend.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;



//DTO to send login response to client
public record UserLoggedInDTO(

    @NotBlank(message = "First Name must be filled")
    String firstName,
    
    @NotBlank(message = "Last Name must be filled")
    String lastName,

    @NotBlank(message = "Email must be filled")
    @Email(message = "Must be a valid email structure")
    String email,

    @NotBlank(message = "Public Id must be filled")
    String publicId



) 
{}
