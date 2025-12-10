package com.osen.favorites_spotify_backend.core.helpers;

import com.osen.favorites_spotify_backend.core.spotify.dtos.Item;
import com.osen.favorites_spotify_backend.core.song.models.Song;

public class SongMapper {

    public static Song toSong(Item item){
        return Song.builder()
                .id(item.id())
                .songName(item.name())
                .artistName(item.artists().getFirst().name())
                .albumName(item.album().name())
                .explicit(item.explicit())
                .songType(item.type())
                .imgAlbumUrl(item.album().images().getFirst().url())
                .build();
    }

}
