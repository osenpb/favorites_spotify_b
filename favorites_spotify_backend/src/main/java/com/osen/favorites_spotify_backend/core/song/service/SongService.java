package com.osen.favorites_spotify_backend.core.song.service;


import com.osen.favorites_spotify_backend.core.dtos.Tracks;
import com.osen.favorites_spotify_backend.core.song.models.Song;

public interface SongService {

    Song save(Tracks tracks);
    Song update(Song song);
    Song findById(Long id);
    Song deleteById(Long id);

}
