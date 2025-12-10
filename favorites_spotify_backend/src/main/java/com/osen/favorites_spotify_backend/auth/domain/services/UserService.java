package com.osen.favorites_spotify_backend.auth.domain.services;

import com.osen.favorites_spotify_backend.auth.application.dtos.UserResponse;
import com.osen.favorites_spotify_backend.auth.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserResponse> findAll();
    User findById(Long id);
    UserResponse save(User user);
    void deleteById(Long id);
    Optional<User> findByEmail(String email);
    boolean update(User user);

}
