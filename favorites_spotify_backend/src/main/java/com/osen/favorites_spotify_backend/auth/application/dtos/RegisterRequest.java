package com.osen.favorites_spotify_backend.auth.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank
        @Size(min = 4, message = "El username debe tener al menos 4 caracteres")
        String username,

        @Email(message = "El formato del correo electrónico es inválido")
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password

) {
}
