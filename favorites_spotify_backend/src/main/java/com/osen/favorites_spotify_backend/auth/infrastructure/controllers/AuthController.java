package com.osen.favorites_spotify_backend.auth.infrastructure.controllers;

import com.osen.favorites_spotify_backend.auth.application.dtos.AuthResponse;
import com.osen.favorites_spotify_backend.auth.application.dtos.LoginRequest;
import com.osen.favorites_spotify_backend.auth.application.dtos.RegisterRequest;
import com.osen.favorites_spotify_backend.auth.application.dtos.UserResponse;
import com.osen.favorites_spotify_backend.auth.application.mappers.AuthMapper;
import com.osen.favorites_spotify_backend.auth.domain.models.User;
import com.osen.favorites_spotify_backend.auth.domain.services.AuthService;
import com.osen.favorites_spotify_backend.auth.domain.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;



    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody @Valid RegisterRequest createUserDto) {
        authService.createUser(createUserDto);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequestDTO) {

        try{
            final User user = userService.findByEmail(loginRequestDTO.email()).orElseThrow();
            UserResponse userResponse = AuthMapper.toDto(user);

            final Map<String, String> tokens = authService.login(loginRequestDTO);
            log.info("Tokens generados correctamente");

            AuthResponse authResponse = new AuthResponse(userResponse, tokens.get("access-token"));

            return ResponseEntity.ok(authResponse);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("No existe el usuario {}", e);
        }

    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request){
        String refreshToken = request.get("refresh");
        Map<String, String> newAccessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(newAccessToken);

    }
}


