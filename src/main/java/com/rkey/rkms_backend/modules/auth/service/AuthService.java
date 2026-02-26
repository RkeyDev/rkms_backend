package com.rkey.rkms_backend.modules.auth.service;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rkey.rkms_backend.modules.auth.dto.UnlockAccountDTO;
import com.rkey.rkms_backend.modules.auth.dto.UserLoggedInDTO;
import com.rkey.rkms_backend.modules.auth.dto.UserLoginDTO;
import com.rkey.rkms_backend.modules.auth.dto.UserRegistrationDTO;
import com.rkey.rkms_backend.modules.auth.entity.UserEntity;
import com.rkey.rkms_backend.modules.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor // Generates constructor for final fields
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public boolean registerUser(UserRegistrationDTO dto) {
        if (!dto.password().equals(dto.confirmPassword())) {
            log.warn("Registration failed: Passwords do not match for email {}", dto.email());
            return false; 
        }

        UserEntity user = new UserEntity();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setEncodedPassword(passwordEncoder.encode(dto.password()));
        // Set default locked state if your business logic requires it
        user.setLocked(true); 

        userRepository.save(user);
        log.info("User registered successfully: {}", dto.email());
        return true;
    }

    public boolean isPasswordValid(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }

    public Optional<UserLoggedInDTO> loginUser(UserLoginDTO dto) {
        return userRepository.findByEmail(dto.email())
                .filter(user -> !user.isLocked())
                .filter(user -> isPasswordValid(dto.password(), user.getEncodedPassword()))
                .map(this::convertToLoggedInDTO);
    }

    @Transactional
    public Optional<UserLoggedInDTO> unlockAccount(UnlockAccountDTO dto) {
        return userRepository.findByUnlockId(dto.unlockId())
                .map(user -> {
                    user.setLocked(false);
                    // No need to call save() manually if @Transactional is used on a managed entity,
                    // but keeping it is fine for clarity.
                    userRepository.save(user);
                    log.info("Account unlocked for user: {}", user.getEmail());
                    return convertToLoggedInDTO(user);
                });
    }

    /**
     * Private helper to keep the logic DRY (Don't Repeat Yourself).
     */
    private UserLoggedInDTO convertToLoggedInDTO(UserEntity user) {
        return new UserLoggedInDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPublicId().toString()
        );
    }
}