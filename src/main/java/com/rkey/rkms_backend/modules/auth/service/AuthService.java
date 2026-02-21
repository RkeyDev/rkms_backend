package com.rkey.rkms_backend.modules.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rkey.rkms_backend.modules.auth.dto.UserRegistrationDTO;
import com.rkey.rkms_backend.modules.auth.entity.UserEntity;
import com.rkey.rkms_backend.modules.auth.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public boolean registerUser(UserRegistrationDTO dto){
        String encodedPassword = passwordEncoder.encode(dto.password()); // Stroe hashed Password

        UserEntity userEntity = new UserEntity();

        // Save dto data in DB
        userEntity.setFirstName(dto.firstName());
        userEntity.setLastName(dto.lastName());
        userEntity.setEmail(dto.email());
        userEntity.setEncodedPassword(encodedPassword);

        try{
            userRepository.save(userEntity);
            log.info("User successfully created! Email: {}", dto.email());
            return true;
        }
        catch(IllegalArgumentException e){
            log.error("Failed to save user in DB. Email: {}",dto.email());
            return false;
        }
    }

}