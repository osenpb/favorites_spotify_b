package com.osen.favorites_spotify_backend.core.user_song.serviceImpl;

import com.osen.favorites_spotify_backend.auth.domain.exceptions.SongNotFoundException;
import com.osen.favorites_spotify_backend.auth.domain.exceptions.UserNotFoundException;
import com.osen.favorites_spotify_backend.auth.domain.models.User;
import com.osen.favorites_spotify_backend.auth.domain.repository.UserRepository;
import com.osen.favorites_spotify_backend.core.spotify.dtos.Item;
import com.osen.favorites_spotify_backend.core.helpers.SongMapper;
import com.osen.favorites_spotify_backend.core.song.models.Song;
import com.osen.favorites_spotify_backend.core.song.repository.SongRepository;
import com.osen.favorites_spotify_backend.core.spotify.service.SpotifyService;
import com.osen.favorites_spotify_backend.core.user_song.service.UserSongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
        log.info("➡️ Eliminando canción. userId={}, songId={}", userId, songId);

            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
            Song sondToRemove = songRepository.findById(songId).orElseThrow(()-> new SongNotFoundException("Song not found with id: " + songId));
            user.getFavoriteSongs().remove(sondToRemove);
            userRepository.save(user);
            return true;

    }

    @Override
    public List<Song> findFavoriteSongsByUserId(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User not found"));

        return user.getFavoriteSongs();
    }
}
