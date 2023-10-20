package com.chatop.controllers;

import com.chatop.authservices.custom_user_details_service.CustomUserDetailsService;
import com.chatop.dtos.AuthRequest;
import com.chatop.dtos.ErrorMessage;
import com.chatop.dtos.Token;
import com.chatop.securityconfigs.jwtutil.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public LoginController(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) throws IOException {
        if(authRequest.email() == null || authRequest.password() == null) {
            return new ResponseEntity<>(new ErrorMessage("Error"), HttpStatus.UNAUTHORIZED);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password()));

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect credentials !!");
        } catch (DisabledException disabledException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found, register User first !");
            return null;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.email());

        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        log.info("Successfully authenticated and sent the token");

        return ResponseEntity.ok(new Token(jwt));
    }

}
