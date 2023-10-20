package com.chatop.authservices.custom_user_details_service;

import com.chatop.models.User;
import com.chatop.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service(value="customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User not found in DB with email:{%s} ".formatted(userEmail));
        }

        return new org.springframework.security.core.userdetails.User(
                user.get().getEmail(), user.get().getPassword(), new ArrayList<>());
    }
}
