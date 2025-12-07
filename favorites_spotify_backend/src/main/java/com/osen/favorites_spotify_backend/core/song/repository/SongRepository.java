package com.osen.favorites_spotify_backend.core.song.repository;

import com.osen.favorites_spotify_backend.core.song.models.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, String> {
}
