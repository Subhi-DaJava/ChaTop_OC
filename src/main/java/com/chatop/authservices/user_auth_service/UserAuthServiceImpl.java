package com.chatop.authservices.user_auth_service;

import com.chatop.authservices.custom_user_details_service.CustomUserDetailsService;
import com.chatop.dtos.*;
import com.chatop.exceptions.UnauthorizedUserException;
import com.chatop.exceptions.UserAlreadyExistsException;
import com.chatop.models.User;
import com.chatop.repositories.UserRepository;
import com.chatop.securityconfigs.jwtutil.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public UserAuthServiceImpl(AuthenticationManager authenticationManager,
                               UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               CustomUserDetailsService userDetailsService,
                               JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    public Token register(SignUpRequest signUpRequest) throws UserAlreadyExistsException {
        Optional<User> userFindByEmail = userRepository.findByEmail(signUpRequest.email());

        if (userFindByEmail.isPresent()) {
            log.error("User with email {} already exists in DB !!", signUpRequest.email());
            throw new UserAlreadyExistsException("User with email: {%s} already exists in DB!!".formatted(signUpRequest.email()));
        }

        if(signUpRequest.name() == null || signUpRequest.email() == null || signUpRequest.password() == null) {
            log.error("Fields cannot be null !!");
            throw new IllegalArgumentException("User name cannot be null or blank");
        }

        User user = User.builder()
                .name(signUpRequest.name())
                .email(signUpRequest.email())
                .password(passwordEncoder.encode(signUpRequest.password()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        log.info("Saving user with email {}", signUpRequest.email());

        String token = jwtService.generateToken(signUpRequest.email());
        log.info("Token generated for this new user with name: {}", signUpRequest.name());
        return new Token(token);
    }

    @Override
    public Token login(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password()));
            log.info("User with email {} authenticated successfully", authRequest.email());
        } catch (BadCredentialsException e) {
            log.error("Incorrect credentials !!");
            throw new BadCredentialsException("Incorrect credentials !!");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.email());

        final String token = jwtService.generateToken(userDetails.getUsername());

        log.info("Successfully authenticated and sent the token");
        return new Token(token);
    }

    @Override
    public MeResponse retrieveProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return new MeResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
        }
        throw new UnauthorizedUserException("User is not authenticated");

    }
}
