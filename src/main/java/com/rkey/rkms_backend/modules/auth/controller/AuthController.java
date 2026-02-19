package com.rkey.rkms_backend.modules.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class AuthController {

    


    @GetMapping
    public String getAuthControllerStatus() {
        
        return "AuthController service is UP";
    }
}