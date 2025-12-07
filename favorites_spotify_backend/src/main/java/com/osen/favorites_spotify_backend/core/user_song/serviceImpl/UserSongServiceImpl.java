package com.osen.favorites_spotify_backend.core.user_song.serviceImpl;

import com.osen.favorites_spotify_backend.auth.domain.models.User;
import com.osen.favorites_spotify_backend.auth.domain.repository.UserRepository;
import com.osen.favorites_spotify_backend.core.dtos.Item;
import com.osen.favorites_spotify_backend.core.helpers.SongMapper;
import com.osen.favorites_spotify_backend.core.song.models.Song;
import com.osen.favorites_spotify_backend.core.song.repository.SongRepository;
import com.osen.favorites_spotify_backend.core.spotify.SpotifyService;
import com.osen.favorites_spotify_backend.core.user_song.service.UserSongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserSongServiceImpl implements UserSongService {

    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final SpotifyService spotifyService;

    @Override
    public boolean saveSongToFavorite(Long userId, String songId, String songName) {
        User user = userRepository.findById(userId).orElseThrow();
        try{
            Item itemStillNotSong = spotifyService.searchTracks(songName).tracks().items()
                    .stream().filter(item -> item.id().equals(songId)).findFirst().orElseThrow();
            Song song = SongMapper.toSong(itemStillNotSong);

            /*
             * It's neccesary to save the song first, bc the song could not exist in the DB, this
             * works using foreign keys related the own DB,
             * and if we try to add it to the user's favorite songs, it will throw an error,
             * or just won't work.
             */
            songRepository.save(song);
            user.getFavoriteSongs().add(song);
            userRepository.save(user);

            return true;
        }catch (Exception e) {
            throw new RuntimeException("Error al guardar la cancion", e);
        }
    }

    @Override
    public boolean deleteSongFromFavorites(Long userId, String songId) {
        try{
            User user = userRepository.findById(userId).orElseThrow();
            Song sondToRemove = songRepository.findById(songId).orElseThrow();
            user.getFavoriteSongs().remove(sondToRemove);

            return true;
        }catch (Exception e) {
            throw new RuntimeException("Error al eliminar la cancion de favoritos", e);
        }
    }
}
