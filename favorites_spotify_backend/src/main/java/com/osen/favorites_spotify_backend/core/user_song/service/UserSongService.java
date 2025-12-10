package com.osen.favorites_spotify_backend.core.user_song.service;

import com.osen.favorites_spotify_backend.core.song.models.Song;

import java.util.List;

public interface UserSongService {

    boolean saveSongToFavorite(Long userId, String songId, String songName);
    boolean deleteSongFromFavorites(Long userId, String songId);
    List<Song> findFavoriteSongsByUserId(Long userId);
}
