package com.osen.favorites_spotify_backend.core.dtos;

import java.util.List;

public record Item(
        String id,
        Album album,
        List<Artist> artists,
        Boolean explicit,
        String name,
        String type

) {
}
