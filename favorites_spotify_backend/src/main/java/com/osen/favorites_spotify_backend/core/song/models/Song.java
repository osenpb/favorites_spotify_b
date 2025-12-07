package com.osen.favorites_spotify_backend.core.song.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "songs")
public class Song {

    @Id
    private String id;

    private String songName;
    private String artistName;

    private String albumName;
    private Boolean explicit;
    private String songType;
    private String imgAlbumUrl;




}
