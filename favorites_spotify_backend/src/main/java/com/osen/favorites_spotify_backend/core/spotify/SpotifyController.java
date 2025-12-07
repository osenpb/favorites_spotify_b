package com.osen.favorites_spotify_backend.core.spotify;


import com.osen.favorites_spotify_backend.core.dtos.SpotifyTrack;
import com.osen.favorites_spotify_backend.core.dtos.Tracks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spotify")
public class SpotifyController {

    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/search")
    public ResponseEntity<Tracks> search(@RequestParam String songName) {
        SpotifyTrack response = spotifyService.searchTracks(songName);
        return ResponseEntity.ok(response.tracks()); // devuelve solo los tracks
    }

    @GetMapping("/saludo")
    public ResponseEntity<String> saludo(){
        return new ResponseEntity<>("Servicio de Spotify activo", HttpStatus.OK);
    }

}