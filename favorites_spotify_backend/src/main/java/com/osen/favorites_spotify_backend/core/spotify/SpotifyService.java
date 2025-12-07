package com.osen.favorites_spotify_backend.core.spotify;



import com.osen.favorites_spotify_backend.core.dtos.SpotifyTrack;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
//import tools.jackson.databind.JsonNode;


@Service
public class SpotifyService {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    private String accessToken;

    private final WebClient webClient;

    public SpotifyService(WebClient webClient) {
        this.webClient = webClient;
    }

    // Con esto llamo al metodo al iniciar el servicio
    @PostConstruct
    private void init() {
        this.accessToken = getAccessToken();
    }

    private String getAccessToken() {
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());

        return WebClient.create()
                .post()
                .uri("https://accounts.spotify.com/api/token")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuth)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .bodyValue("grant_type=client_credentials")
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> json.get("access_token").asString()) // antes era asText()
                .block();
    }
    // devuelve una lista de canciones que coinciden con el songName
    public SpotifyTrack searchTracks(String songName){

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/search")
                        .queryParam("q", songName)
                        .queryParam("type", "track")
                        .queryParam("limit", 5)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(SpotifyTrack.class)
                .block();

    }

}

