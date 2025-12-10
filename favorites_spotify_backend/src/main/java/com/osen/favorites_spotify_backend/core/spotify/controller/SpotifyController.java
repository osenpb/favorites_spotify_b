package com.osen.favorites_spotify_backend.core.spotify.controller;


import com.osen.favorites_spotify_backend.core.spotify.dtos.SpotifyTrack;
import com.osen.favorites_spotify_backend.core.spotify.dtos.Tracks;
import com.osen.favorites_spotify_backend.core.spotify.service.SpotifyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/spotify")
public class SpotifyController {

    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @PostMapping("/search")
    public ResponseEntity<Tracks> search(@RequestBody String songName) {
        SpotifyTrack response = spotifyService.searchTracks(songName);
        return ResponseEntity.ok(response.tracks()); // devuelve solo los tracks/items
    }

    @GetMapping("/saludo")
    public ResponseEntity<String> saludo(){
        return new ResponseEntity<>("Servicio de Spotify activo", HttpStatus.OK);
    }

}