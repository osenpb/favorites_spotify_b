package com.osen.favorites_spotify_backend.auth.domain.services;

import com.osen.favorites_spotify_backend.auth.application.dtos.UserResponse;
import com.osen.favorites_spotify_backend.auth.application.mappers.AuthMapper;
import com.osen.favorites_spotify_backend.auth.domain.models.User;
import com.osen.favorites_spotify_backend.auth.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{


    private final UserRepository userRepository;

    @Override
    public List<UserResponse> findAll() {

        List<User> userList = userRepository.findAll();

        return  userList.stream()
                    .map((AuthMapper::toDto))
                    .collect(Collectors.toList());
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User  not found with id: " + id));
    }
    @Override
    public UserResponse save(User user) {
        userRepository.save(user);
        return AuthMapper.toDto(user);
    }


    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    // Here I can add a UpdateUserRequest to specify wich fields update
    @Override
    public boolean update(User user) {
        if(userRepository.findById(user.getId()).isPresent()){
            userRepository.save(user);
            return true;
        }

        return false;
    }



}
