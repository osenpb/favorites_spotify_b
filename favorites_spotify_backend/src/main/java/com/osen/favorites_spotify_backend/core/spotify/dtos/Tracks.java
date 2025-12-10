package com.osen.favorites_spotify_backend.core.spotify.dtos;

import java.util.List;

public record Tracks(
        List<Item> items
) {
}
