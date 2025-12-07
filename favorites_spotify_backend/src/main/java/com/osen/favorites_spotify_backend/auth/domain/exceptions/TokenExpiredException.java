package com.osen.favorites_spotify_backend.auth.domain.exceptions;

public class TokenExpiredException extends RuntimeException{

    public TokenExpiredException(String message) {
        super(message);
    }

}
