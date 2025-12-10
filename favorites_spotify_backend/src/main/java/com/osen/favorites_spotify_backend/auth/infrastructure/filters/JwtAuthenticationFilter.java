package com.osen.favorites_spotify_backend.auth.infrastructure.filters;

import com.osen.favorites_spotify_backend.auth.domain.exceptions.TokenExpiredException;
import com.osen.favorites_spotify_backend.auth.domain.services.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver; // para manejar excepciones dentro del filtro. usalo con precaucion

    public JwtAuthenticationFilter(AuthService authService,
                                   UserDetailsService userDetailsService,
                                   @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.authService = authService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }
    // no he usado el required args construct para q se note que esta clase tiene el constructor explicito

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {

        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            final Optional<String> token = resolveTokenFromHeader(request);

            // Si no hay token o es inválido, que Spring Security maneje la autorización
            if (token.isEmpty() || !authService.validateToken(token.get())) {
                filterChain.doFilter(request, response);
                return;
            }

            String userName = authService.getUserFromToken(token.get());

            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

            authenticationToken.setDetails(userDetails);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        } catch (TokenExpiredException | JwtException exception) {

            this.handlerExceptionResolver.resolveException(request, response, null, exception);


        }
    }


    //si en lugar de gestionar mediante cookies quieres hacerlo con authorization, solo cambias este metodo
    private Optional<String> resolveTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return Optional.of(bearerToken.substring(7)); // quitar "Bearer "
        }
        return Optional.empty();
    }
}
