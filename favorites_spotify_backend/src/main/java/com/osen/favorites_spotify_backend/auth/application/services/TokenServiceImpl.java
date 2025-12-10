package com.osen.favorites_spotify_backend.auth.application.services;


import com.osen.favorites_spotify_backend.auth.domain.exceptions.TokenExpiredException;
import com.osen.favorites_spotify_backend.auth.domain.models.User;
import com.osen.favorites_spotify_backend.auth.domain.repository.UserRepository;
import com.osen.favorites_spotify_backend.auth.domain.services.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private int jwtExpiration;

    @Value("${application.security.jwt.refresh-expiration}")
    private int jwtRefreshExpiration;

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    private final UserRepository userRepository;

    @Override
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        User currenUser = (User) authentication.getPrincipal();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(currenUser.getEmail())
                .issuedAt(now)
                .expiresAt(now.plus(jwtExpiration, ChronoUnit.MINUTES))
                .claim("roles", roles)
                .build();

        var jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

    @Override
    public String getUserFromToken(String token) {
        Jwt jwtToken = jwtDecoder.decode(token);
        return jwtToken.getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            jwtDecoder.decode(token);
            return true;

        } catch (JwtException exception) {
            throw new TokenExpiredException("Error while trying to validate token");
        }
    }

    @Override
    public String generateRefreshToken(User user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getEmail())
                .issuedAt(now) // fecha de creacion
                .expiresAt(now.plus(jwtRefreshExpiration, ChronoUnit.MINUTES)) // el ChronoUnit convierte mi valor a minutos, sin eso seria solo milisegundos x defecto
                .claim("type", "refresh")
                .build();

        var jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

    @Override
    public Map<String, String> refreshToken(String refreshToken) {
        try {
            Jwt jwtRefreshToken = jwtDecoder.decode(refreshToken);

            String type = jwtRefreshToken.getClaim("type");

            if (!"refresh".equals(type)) {
                log.info("type incorrecto: {}", type);
                throw new JwtException("Invalid token type");
            }

            String email = jwtRefreshToken.getSubject();
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new UsernameNotFoundException("Usuario no encontrado"));

            String newAccessToken = generateToken(
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

            return Map.of(
                    "token", newAccessToken); // luego puedes cambiar el nombre

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

