package com.nhandn.backend.service;

import com.nhandn.backend.dto.UserRequest;
import com.nhandn.backend.dto.UserResponse;
import com.nhandn.backend.exception.ResourceNotFoundException;
import com.nhandn.backend.repository.ScoreRepository;
import com.nhandn.backend.repository.TokenRepository;
import com.nhandn.backend.repository.UserRepository;
import com.nhandn.backend.entity.User;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final TokenRepository tokenRepository;

    private final ScoreRepository scoreRepository;

    @Transactional
    @Override
    public UserResponse create(UserRequest request) throws Exception {
        var findUser = repository.findByEmail(request.getEmail());
        if (findUser.isPresent()) {
            throw new Exception(String.format("User existed this email %s", request.getEmail()));
        }
        var user = User.builder()
                .name(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var savedUser = repository.save(user);
        return UserResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();
    }

    @Override
    public UserResponse getUser(Integer id) {
        var user = repository.findById(id).orElseThrow();
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();

    }

    @Override
    public Page<UserResponse> getAllUser(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return repository.findAll(pageable)
                .map(u -> UserResponse.builder()
                        .id(u.getId())
                        .username(u.getName())
                        .email(u.getEmail())
                        .role(u.getRole().name())
                        .build());
    }

    @Transactional
    @Override
    public void updateUser(UserRequest request) {
        var user = repository.findById(request.getId()).orElseThrow();
        user.setName(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        repository.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(String email) throws Exception {
        var user = repository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Not found for this mail : " + email));
        boolean userTested = scoreRepository.isUserTested(user.getId());
        if (userTested) {
            throw new Exception(email + " made test");
        }
        tokenRepository.deleteAll(user.getTokens());
        repository.delete(user);
    }
}
