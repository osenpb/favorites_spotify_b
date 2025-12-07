package com.osen.favorites_spotify_backend.core.song.serviceImpl;

import com.osen.favorites_spotify_backend.core.dtos.Tracks;
import com.osen.favorites_spotify_backend.core.song.models.Song;
import com.osen.favorites_spotify_backend.core.song.service.SongService;
import org.springframework.stereotype.Service;

@Service
public class SongServiceImpl implements SongService {


    @Override
    public Song save(Tracks tracks) {
        return null;
    }

    @Override
    public Song update(Song song) {
        return null;
    }

    @Override
    public Song findById(Long id) {
        return null;
    }

    @Override
    public Song deleteById(Long id) {
        return null;
    }
}
