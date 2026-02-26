package com.rkey.rkms_backend.modules.auth.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record UnlockAccountDTO(
    @NotBlank
    UUID unlockId
) {}
