package com.osen.favorites_spotify_backend.auth.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email
        @NotBlank
        String email,

        @NotBlank
        String password
) {
}
