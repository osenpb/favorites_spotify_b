package com.osen.favorites_spotify_backend.core.song.service;


import com.osen.favorites_spotify_backend.core.spotify.dtos.Tracks;
import com.osen.favorites_spotify_backend.core.song.models.Song;

public interface SongService {

    Song save(Tracks tracks);
    //Boolean update(Song song);
    Song findById(String id);
    Boolean deleteById(String id);

}
