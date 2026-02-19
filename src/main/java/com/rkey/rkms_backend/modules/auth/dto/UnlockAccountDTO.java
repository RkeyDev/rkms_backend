package com.rkey.rkms_backend.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record UnlockAccountDTO(
    @NotBlank
    String userId
) {}
