package com.osen.favorites_spotify_backend.core.user_song.controller;


import com.osen.favorites_spotify_backend.core.song.repository.SongRepository;
import com.osen.favorites_spotify_backend.core.user_song.dtos.FavoriteSongRequest;
import com.osen.favorites_spotify_backend.core.user_song.service.UserSongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class UserSongRestController {

    private final UserSongService userSongService;
    private final SongRepository songRepository;

    @PostMapping("/add/{userId}")
    public ResponseEntity<Boolean> addSongToFavorites(@PathVariable Long userId, @RequestBody FavoriteSongRequest favoriteSongRequest) {

        boolean result = userSongService.saveSongToFavorite(userId, favoriteSongRequest.songId(), favoriteSongRequest.songName());
        return ResponseEntity.ok(result);
    }

}
