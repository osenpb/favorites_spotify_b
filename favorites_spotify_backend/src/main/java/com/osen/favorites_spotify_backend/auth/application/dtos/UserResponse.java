package com.osen.favorites_spotify_backend.auth.application.dtos;

import com.osen.favorites_spotify_backend.auth.domain.models.Role;

public record UserResponse(
        Long id,
        String username,
        String email,
        Role role
) {
}
