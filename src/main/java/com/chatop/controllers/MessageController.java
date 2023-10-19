package com.chatop.controllers;

import com.chatop.dtos.MessageResponse;
import com.chatop.models.Message;
import com.chatop.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Slf4j
public class MessageController {

    private final MessageRepository messageRepository;

    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(@RequestBody Message message, String token) {
        messageRepository.save(message);
        log.info("Message send with success");
        if(message.getUser().getId() == null || message.getMessage() == null || message.getRental().getId() == null) {
            return new ResponseEntity<>("{}", HttpStatusCode.valueOf(400));
        }
        return new ResponseEntity<>(new MessageResponse("Message send with success"), HttpStatusCode.valueOf(200));
    }

}
