package com.osen.favorites_spotify_backend.core.user_song.dtos;

public record FavoriteSongRequest(
        String songId,
        String songName
) {
}
