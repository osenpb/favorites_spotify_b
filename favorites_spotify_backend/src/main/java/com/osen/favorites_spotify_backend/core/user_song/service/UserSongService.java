package com.osen.favorites_spotify_backend.core.user_song.service;

public interface UserSongService {

    boolean saveSongToFavorite(Long userId, String songId, String songName);
    boolean deleteSongFromFavorites(Long userId, String songId);
}
