package com.chatop.controllers;

import com.chatop.dtos.MessageDTO;
import com.chatop.dtos.MessageResponse;
import com.chatop.services.message.MessageService;
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

    private final MessageService messageService;

    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO message) {

        if(message.user_id() == null || message.message() == null || message.rental_id() == null) {
            log.error("Message not send, verify your input Data");
            return new ResponseEntity<>("{}", HttpStatusCode.valueOf(400));
        }
        messageService.sendMessage(message);

        log.info("Message send with success");
        return new ResponseEntity<>(new MessageResponse("Message send with success"), HttpStatusCode.valueOf(200));
    }

}
