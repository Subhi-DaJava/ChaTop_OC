package com.chatop.controllers;

import com.chatop.dtos.AuthResponse;
import com.chatop.models.User;
import com.chatop.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/auth/me")
    public ResponseEntity<?> retrieveUser(String token) {
        Optional<User> user = userRepository.findById(1);
        if(user.isEmpty()) {
            return new ResponseEntity<>("{}", HttpStatusCode.valueOf(400));
        }

        AuthResponse authResponse =
                new AuthResponse(user.get().getId(), user.get().getName(), user.get().getEmail(), user.get().getCreatedAt(), user.get().getUpdatedAt());

        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<AuthResponse> retrieveUserById(@PathVariable Integer id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        AuthResponse authResponse =
                new AuthResponse(user.get().getId(), user.get().getName(), user.get().getEmail(), user.get().getCreatedAt(), user.get().getUpdatedAt());
        return ResponseEntity.ok(authResponse);
    }
}
