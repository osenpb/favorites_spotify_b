package com.osen.favorites_spotify_backend.auth.domain.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;
    private String message;
    private String details;

}
