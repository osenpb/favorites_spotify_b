package com.osen.favorites_spotify_backend.core.user_song.controller;


import com.osen.favorites_spotify_backend.auth.domain.models.User;
import com.osen.favorites_spotify_backend.auth.domain.services.UserService;
import com.osen.favorites_spotify_backend.core.song.repository.SongRepository;
import com.osen.favorites_spotify_backend.core.user_song.dtos.FavoriteSongRequest;
import com.osen.favorites_spotify_backend.core.user_song.service.UserSongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/spotify/favorites")
@RequiredArgsConstructor
public class UserSongRestController {

    private final UserService userService;
    private final UserSongService userSongService;
    private final SongRepository songRepository;

    @PostMapping("/add")
    public ResponseEntity<Boolean> addSongToFavorites(@AuthenticationPrincipal User user, @RequestBody FavoriteSongRequest favoriteSongRequest) {

        boolean result = userSongService.saveSongToFavorite(user.getId(), favoriteSongRequest.songId(), favoriteSongRequest.songName());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Boolean> removeSongFromFavorites(@AuthenticationPrincipal User user, @RequestBody String songId) {

        log.info("Song ID to remove: {}", songId);
        boolean result = userSongService.deleteSongFromFavorites(user.getId(), songId);
        return ResponseEntity.ok(result);

    }

    @GetMapping("/list")
    public ResponseEntity<?> getFavoriteSongs(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userSongService.findFavoriteSongsByUserId(user.getId()));

    }

}
