package com.osen.favorites_spotify_backend.core.user_song.service;

import com.osen.favorites_spotify_backend.core.song.models.Song;

import java.util.List;
import java.util.Set;

public interface UserSongService {

    boolean saveSongToFavorite(Long userId, String songId, String songName);
    boolean deleteSongFromFavorites(Long userId, String songId);
    Set<Song> findFavoriteSongsByUserId(Long userId);
}
