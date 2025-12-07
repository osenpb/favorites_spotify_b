package com.osen.favorites_spotify_backend.auth.application.services;


import com.osen.favorites_spotify_backend.auth.application.dtos.LoginRequest;
import com.osen.favorites_spotify_backend.auth.application.dtos.RegisterRequest;
import com.osen.favorites_spotify_backend.auth.application.mappers.AuthMapper;
import com.osen.favorites_spotify_backend.auth.domain.exceptions.TokenExpiredException;
import com.osen.favorites_spotify_backend.auth.domain.models.Role;
import com.osen.favorites_spotify_backend.auth.domain.models.User;
import com.osen.favorites_spotify_backend.auth.domain.repository.UserRepository;
import com.osen.favorites_spotify_backend.auth.domain.services.AuthService;
import com.osen.favorites_spotify_backend.auth.domain.services.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationConfiguration authenticationConfiguration;


    @Override
    public void createUser(@Valid RegisterRequest createUserDto) {
        final User createUser = AuthMapper.fromDto(createUserDto);
        createUser.setEmail(createUserDto.email());
        createUser.setPassword(passwordEncoder.encode(createUserDto.password()));
        createUser.setRole(Role.USER);
        final User user = userRepository.save(createUser);
        log.info("[USER] : User successfully created with id {}", user.getId());

    }
    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("[USER] : User not found with id {}", id);
                    return new UsernameNotFoundException("User not found"); // exception personalizada
                });
    }

    @Override
    public Map<String, String> refreshToken(String refreshToken) {
        return tokenService.refreshToken(refreshToken);
    }


    @Override
    public Map<String, String> login(final @Valid LoginRequest loginRequestDTO) {
        try {
            final AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager(); // el manager es el verificador del authentication
            final Authentication authRequest = AuthMapper.fromDto(loginRequestDTO); //authentication es como el dni
            final Authentication authentication = authenticationManager.authenticate(authRequest); // chequea la autenticidad

            User user = (User) authentication.getPrincipal();

            String token = tokenService.generateToken(authentication);
            //String refreshToken = tokenService.generateRefreshToken(user);

            return Map.of(

                    "token", token
                    // sin refreshToken x ahora
            );

        } catch (BadCredentialsException e) {
            log.error("ERROR: Credenciales incorrectas para: {}", loginRequestDTO.email());
            throw new BadCredentialsException("Credenciales inválidas", e);
        } catch (UsernameNotFoundException e) {
            log.error("ERROR: Usuario no encontrado: {}", loginRequestDTO.email());
            throw new UsernameNotFoundException("Usuario no encontrado", e);
        } catch (DisabledException e) {
            log.error("ERROR: Usuario deshabilitado: {}", loginRequestDTO.email());
            throw new DisabledException("Usuario deshabilitado", e);
        } catch (Exception e) {
            log.error("ERROR INESPERADO durante login: {}", e.getMessage(), e);
            throw new RuntimeException("Error en autenticación generico", e);
        }
    }

    @Override
    public boolean validateToken(final String token) throws TokenExpiredException {

            return tokenService.validateToken(token);
    }

    @Override
    public String getUserFromToken(final String token) {
        return tokenService.getUserFromToken(token);
    }


    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.error("[USER] : User not found with email {}", username);
                    return new UsernameNotFoundException("User not found");
                });
    }
}
