package com.osen.favorites_spotify_backend.auth.application.dtos;

public record AuthResponse(
        UserResponse userResponse,
        String token

) {
}
