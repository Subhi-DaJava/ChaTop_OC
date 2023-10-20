package com.chatop.controllers;

import com.chatop.authservices.user_auth_service.UserAuthService;
import com.chatop.dtos.AuthResponse;
import com.chatop.dtos.Token;
import com.chatop.dtos.UserDTO;
import com.chatop.exceptions.UserAlreadyExistsException;
import com.chatop.models.User;
import com.chatop.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserRepository userRepository;
    private final UserAuthService userAuthService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) throws UserAlreadyExistsException {

        if(userDTO.name() == null || userDTO.email() == null || userDTO.password() == null) {
            return new ResponseEntity<>("{}", HttpStatus.BAD_REQUEST);
        }

        Token token = userAuthService.register(userDTO);

        return ResponseEntity.ok(token);
    }

    @GetMapping("/auth/me")
    public ResponseEntity<?> retrieveUser() {
       AuthResponse authResponse = userAuthService.retrieveProfile();

       if (authResponse == null) {
           return new ResponseEntity<>("{}", HttpStatus.UNAUTHORIZED);
       }
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
