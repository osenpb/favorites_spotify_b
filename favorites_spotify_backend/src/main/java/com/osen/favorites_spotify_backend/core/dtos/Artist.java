package com.osen.favorites_spotify_backend.core.dtos;

public record Artist(
    String href,
    String id,
    String name,
    String type // tmb podrias agregar uri
) {
}
