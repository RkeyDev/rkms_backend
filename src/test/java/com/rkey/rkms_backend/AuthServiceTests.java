package com.rkey.rkms_backend;

import java.util.Optional;

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
    void registerUser_NormalScenerio(){
        UserRegistrationDTO user = new UserRegistrationDTO("Roei","Kleiner","roei1576@gmail.com", "roeiPassword", "roeiPassword");
        when(passwordEncoder.encode(user.password())).thenReturn("hashedPassword123");

        boolean isRegistered = authService.registerUser(user);

        assertEquals(isRegistered, true);

        verify(passwordEncoder, times(1)).encode(user.password());
        verify(userRepository, times(1)).save(any(UserEntity.class));

    }

    @Test
    @DisplayName("Should return true if passwords are matching")
    void validatePasswords_SamePasswordScenerio(){
        String email = "roei1576@gmail.com";
        String password = "roeiPassword";
        String mockHashedPassword = "hashedPassword2323";
        UserEntity user = new UserEntity();

        user.setEncodedPassword(mockHashedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password,mockHashedPassword)).thenReturn(true);

        boolean isValid = authService.isPaswordValid("roei1576@gmail.com", "roeiPassword");
        
        assertEquals(isValid, true);

        verify(userRepository,times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, mockHashedPassword);


    }

    @Test
    @DisplayName("Should return true if passwords are matching")
    void validatePasswords_WrongPasswordScenerio(){
        String email = "roei1576@gmail.com";
        String password = "roeiPassword";
        String mockHashedPassword = "hashedPassword2323";
        UserEntity user = new UserEntity();

        user.setEncodedPassword(mockHashedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password,mockHashedPassword)).thenReturn(false);

        boolean isValid = authService.isPaswordValid("roei1576@gmail.com", "roeiPassword");
        
        assertEquals(isValid, false);

        verify(userRepository,times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, mockHashedPassword);


    }

}
