package com.osen.favorites_spotify_backend.core.spotify.service;

import com.osen.favorites_spotify_backend.core.spotify.dtos.SpotifyTrack;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import tools.jackson.databind.JsonNode;

@Service
public class SpotifyService {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Value("${spotify.api.base-url}")
    private String baseUrl;

    private String accessToken;

    private final WebClient.Builder webClientBuilder;
    private WebClient webClient;

    public SpotifyService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @PostConstruct
    private void init() {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    private String getValidToken() {
        if (accessToken == null) {
            accessToken = fetchToken();
        }
        return accessToken;
    }

    private String fetchToken() {
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
                .map(json -> json.get("access_token").asText())
                .block();
    }

    public SpotifyTrack searchTracks(String songName) {

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/search")
                            .queryParam("q", songName)
                            .queryParam("type", "track")
                            .queryParam("limit", 5)
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + getValidToken())
                    .retrieve()
                    .bodyToMono(SpotifyTrack.class)
                    .block();

        } catch (WebClientResponseException.Unauthorized e) {

            accessToken = fetchToken();

            return webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/search")
                            .queryParam("q", songName)
                            .queryParam("type", "track")
                            .queryParam("limit", 5)
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(SpotifyTrack.class)
                    .retry(1)
                    .block();
        }
    }
}
