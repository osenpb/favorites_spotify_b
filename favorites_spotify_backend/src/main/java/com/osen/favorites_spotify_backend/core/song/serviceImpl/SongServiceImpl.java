package com.osen.favorites_spotify_backend.core.song.serviceImpl;

import com.osen.favorites_spotify_backend.auth.domain.exceptions.SongNotFoundException;
import com.osen.favorites_spotify_backend.core.helpers.SongMapper;
import com.osen.favorites_spotify_backend.core.song.repository.SongRepository;
import com.osen.favorites_spotify_backend.core.spotify.dtos.Tracks;
import com.osen.favorites_spotify_backend.core.song.models.Song;
import com.osen.favorites_spotify_backend.core.song.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    @Override
    public Song save(Tracks tracks) {
        return null;
    }

//    @Override
//    public Song update(Song song) {
//        return new Song();
//    }

    @Override
    public Song findById(String id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song not found with id: " + id));
    }

    @Override
    public Boolean deleteById(String id) {
        if(songRepository.findById(id).isPresent()){
            songRepository.deleteById(id);
            return true;
        }
        return false;

    }
}
