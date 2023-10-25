package com.chatop.authservices.user_auth_service;

import com.chatop.authservices.custom_user_details_service.CustomUserDetailsService;
import com.chatop.dtos.*;
import com.chatop.exceptions.UnauthorizedUserException;
import com.chatop.exceptions.UserAlreadyExistsException;
import com.chatop.models.User;
import com.chatop.repositories.UserRepository;
import com.chatop.securityconfigs.jwtutil.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public UserAuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Token register(UserDTO userDTO) throws UserAlreadyExistsException {
        Optional<User> userFindByEmail = userRepository.findByEmail(userDTO.email());

        if (userFindByEmail.isPresent()) {
            log.error("User with email {} already exists in DB !!", userDTO.email());
            throw new UserAlreadyExistsException("User with email: {%s} already exists in DB!!".formatted(userDTO.email()));
        }

        if(userDTO.name() == null || userDTO.email() == null || userDTO.password() == null) {
            log.error("Fields cannot be null !!");
            throw new IllegalArgumentException("User name cannot be null or blank");
        }

        User user = User.builder()
                .name(userDTO.name())
                .email(userDTO.email())
                .password(passwordEncoder.encode(userDTO.password()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        log.info("Saving user with email {}", userDTO.email());

        String token = jwtUtil.generateToken(userDTO.email());
        log.info("Token generated for this new user with name: {}", userDTO.name());
        return new Token(token);
    }

    @Override
    public AuthResponseDTO<?> login(AuthRequest authRequest) {
        if(authRequest.email() == null || authRequest.password() == null) {
            log.error("Fields cannot be null !!");
            return new AuthResponseDTO<>(new ErrorMessage("error"));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.email());

        if (userDetails == null) {
            log.error("User with email {} does not exist", authRequest.email());
            return new AuthResponseDTO<>(new ErrorMessage("error"));
        }

        String token = jwtUtil.generateToken(authRequest.email());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return new AuthResponseDTO<>(token);
    }

    @Override
    public AuthResponse retrieveProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return new AuthResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
        }
        throw new UnauthorizedUserException("User is not authenticated");

    }
}
