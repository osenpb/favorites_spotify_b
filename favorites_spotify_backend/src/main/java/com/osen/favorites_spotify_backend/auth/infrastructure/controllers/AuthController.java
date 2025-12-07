package com.osen.favorites_spotify_backend.auth.infrastructure.controllers;

import com.osen.favorites_spotify_backend.auth.application.dtos.LoginRequest;
import com.osen.favorites_spotify_backend.auth.application.dtos.RegisterRequest;
import com.osen.favorites_spotify_backend.auth.domain.services.AuthService;
import jakarta.validation.Valid;
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
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody @Valid RegisterRequest createUserDto) {
        authService.createUser(createUserDto);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody @Valid LoginRequest loginRequestDTO) {

        log.info("Email recibido: {}", loginRequestDTO.email());

        try{
            final Map<String, String> tokens = authService.login(loginRequestDTO);
            log.info("Tokens generados correctamente");
            return ResponseEntity.ok(tokens);

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


