package com.rkey.rkms_backend.modules.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rkey.rkms_backend.modules.auth.dto.UnlockAccountDTO;
import com.rkey.rkms_backend.modules.auth.dto.UserLoggedInDTO;
import com.rkey.rkms_backend.modules.auth.dto.UserLoginDTO;
import com.rkey.rkms_backend.modules.auth.dto.UserRegistrationDTO;
import com.rkey.rkms_backend.modules.auth.entity.UserEntity;
import com.rkey.rkms_backend.modules.auth.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    // --- Registration Tests ---

    @Test
    @DisplayName("Should save users in the DB when registering.")
    void registerUser_NormalScenario() {
        UserRegistrationDTO userDto = new UserRegistrationDTO("Roei", "Kleiner", "roei1576@gmail.com", "roeiPassword", "roeiPassword");
        when(passwordEncoder.encode(userDto.password())).thenReturn("hashedPassword123");

        boolean isRegistered = authService.registerUser(userDto);

        assertTrue(isRegistered);
        verify(passwordEncoder, times(1)).encode(userDto.password());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    // --- Password Validation Tests ---

    @Test
    @DisplayName("Should return true if passwords are matching")
    void validatePasswords_SamePasswordScenario() {
        String password = "roeiPassword";
        String mockHashedPassword = "hashedPassword2323";

        when(passwordEncoder.matches(password, mockHashedPassword)).thenReturn(true);

        boolean isValid = authService.isPasswordValid(password, mockHashedPassword);

        assertTrue(isValid);
        verify(passwordEncoder, times(1)).matches(password, mockHashedPassword);
    }

    @Test
    @DisplayName("Should return false if passwords DO NOT match")
    void validatePasswords_WrongPasswordScenario() {
        String password = "roeiPassword";
        String mockHashedPassword = "hashedPassword2323";

        when(passwordEncoder.matches(password, mockHashedPassword)).thenReturn(false);

        boolean isValid = authService.isPasswordValid(password, mockHashedPassword);

        assertFalse(isValid);
        verify(passwordEncoder, times(1)).matches(password, mockHashedPassword);
    }


    @Test
    @DisplayName("Should successfully login the user and return a valid login DTO")
    void loginUser_SuccessScenario() {
        // Arrange
        String email = "roei@gmail.com";
        String rawPassword = "pitiLevy";
        String encodedPassword = "encodedPitiLevy";
        UserLoginDTO dto = new UserLoginDTO(email, rawPassword);

        UserEntity user = createMockUser(email, encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        // Act
        Optional<UserLoggedInDTO> result = authService.loginUser(dto);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user.getFirstName(), result.get().firstName());
        assertEquals(user.getEmail(), result.get().email());
    }

    @Test
    @DisplayName("Should return empty Optional when login fails due to locked account")
    void loginUser_LockedAccount_ReturnsEmpty() {
        // Arrange
        String email = "locked@gmail.com";
        UserLoginDTO dto = new UserLoginDTO(email, "anyPassword");
        UserEntity user = createMockUser(email, "secret");
        user.setLocked(true);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        Optional<UserLoggedInDTO> result = authService.loginUser(dto);

        // Assert
        assertTrue(result.isEmpty());
        // Verify we stop at the filter and don't even check the password
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }


    @Test
    @DisplayName("Should unlock account and return DTO when valid unlockId provided")
    void unlockAccount_ValidId_UnlocksUser() {
        // Arrange
        UUID unlockUUID = UUID.randomUUID();
        UnlockAccountDTO dto = new UnlockAccountDTO(unlockUUID);
        UserEntity user = createMockUser("roei@gmail.com", "hash");
        user.setLocked(true);
        user.setUnlockId(unlockUUID);

        when(userRepository.findByUnlockId(unlockUUID)).thenReturn(Optional.of(user));

        // Act
        Optional<UserLoggedInDTO> result = authService.unlockAccount(dto);

        // Assert
        assertTrue(result.isPresent());
        assertFalse(user.isLocked(), "User should be unlocked in the entity state");
        verify(userRepository).save(user);
    }

    private UserEntity createMockUser(String email, String encodedPassword) {
        UserEntity user = new UserEntity();
        user.setFirstName("Roei");
        user.setLastName("Kleiner");
        user.setEmail(email);
        user.setEncodedPassword(encodedPassword);
        user.setPublicId(UUID.randomUUID());
        user.setLocked(false);
        return user;
    }
}
