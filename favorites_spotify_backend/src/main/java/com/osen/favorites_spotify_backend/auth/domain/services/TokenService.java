package com.osen.favorites_spotify_backend.auth.domain.services;

import com.osen.favorites_spotify_backend.auth.domain.models.User;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface TokenService {
    String generateToken(Authentication authentication);
    String getUserFromToken(String token);
    boolean validateToken(String token);
    String generateRefreshToken(User user);
    Map<String, String> refreshToken(String refreshToken);
}
