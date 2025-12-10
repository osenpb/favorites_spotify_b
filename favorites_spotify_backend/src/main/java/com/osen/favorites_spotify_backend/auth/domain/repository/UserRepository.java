package com.osen.favorites_spotify_backend.auth.domain.repository;

import com.osen.favorites_spotify_backend.auth.domain.models.User;

import com.osen.favorites_spotify_backend.core.song.models.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User save(User user);
    Optional<User> findById(Long id);

}
