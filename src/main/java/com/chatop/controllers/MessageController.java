package com.chatop.controllers;

import com.chatop.models.Message;
import com.chatop.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Slf4j
public class MessageController {

    private final MessageRepository messageRepository;

    @PostMapping("/messages")
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        return new ResponseEntity<>(messageRepository.save(message), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> retrieveAllMessages() {
        return ResponseEntity.ok(messageRepository.findAll());
    }

}
