package com.osen.favorites_spotify_backend.core.dtos;

import java.util.List;

public record Album(
        List<Image> images,
        String name


) {

}
