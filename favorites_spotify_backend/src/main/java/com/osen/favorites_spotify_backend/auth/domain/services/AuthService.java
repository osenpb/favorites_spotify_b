package com.osen.favorites_spotify_backend.auth.domain.services;

import com.osen.favorites_spotify_backend.auth.application.dtos.LoginRequest;
import com.osen.favorites_spotify_backend.auth.application.dtos.RegisterRequest;
import com.osen.favorites_spotify_backend.auth.domain.models.User;
import jakarta.validation.Valid;

import java.util.Map;

public interface AuthService {
    Map<String, String> login(@Valid LoginRequest loginRequestDTO);
    boolean validateToken(String token);
    String getUserFromToken(String token);
    void createUser(@Valid RegisterRequest createUserDto);
    User getUser(Long id);
    Map<String, String> refreshToken(String refreshToken);
}
