package com.chatop.controllers;

import com.chatop.dtos.AuthRequest;
import com.chatop.dtos.ErrorMessage;
import com.chatop.dtos.Token;
import com.chatop.dtos.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class LoginController {

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        if(authRequest.email() == null || authRequest.password() == null) {
            return new ResponseEntity<>(new ErrorMessage("Error"), HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(new Token("jwt"));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        if(userDTO.name() == null || userDTO.email() == null || userDTO.password() == null) {
            return new ResponseEntity<>("{}",HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new Token("jwt"));
    }

}
