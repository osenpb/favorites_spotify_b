package com.osen.favorites_spotify_backend.core.dtos;

import java.util.List;

public record Tracks(
        List<Item> items
) {
}
