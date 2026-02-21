package com.rkey.rkms_backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rkey.rkms_backend.modules.auth.dto.UserRegistrationDTO;
import com.rkey.rkms_backend.modules.auth.entity.UserEntity;
import com.rkey.rkms_backend.modules.auth.repository.UserRepository;
import com.rkey.rkms_backend.modules.auth.service.AuthService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Should save users in the DB when logging in.")
    void registerUSer_NormalScenerio(){
        UserRegistrationDTO user = new UserRegistrationDTO("Roei","Kleiner","roei1576@gmail.com", "roeiPassword", "roeiPassword");
        when(passwordEncoder.encode(user.password())).thenReturn("hashedPassword123");

        boolean isRegistered = authService.registerUser(user);

        assertEquals(isRegistered, true);

        verify(passwordEncoder, times(1)).encode(user.password());
        verify(userRepository, times(1)).save(any(UserEntity.class));

    }

}
