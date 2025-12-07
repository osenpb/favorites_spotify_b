package com.osen.favorites_spotify_backend.auth.infrastructure.controllers;

import com.osen.favorites_spotify_backend.auth.domain.models.User;
import com.osen.favorites_spotify_backend.auth.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserRepository userRepository;

    @PostMapping("/new")
    public ResponseEntity<User> createUser(@RequestBody User user){

        User saveUser = userRepository.save(user);
        return ResponseEntity.ok(saveUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getById(@PathVariable Long userId) {
        return ResponseEntity.ok(userRepository.findById(userId).orElseThrow());
    }

}
