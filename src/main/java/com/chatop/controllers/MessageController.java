package com.chatop.controllers;

import com.chatop.dtos.MessageDTO;
import com.chatop.dtos.MessageResponse;
import com.chatop.exceptions.FiledNotNullOrEmptyException;
import com.chatop.services.message.MessageService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Slf4j
@Tag(name = "Message API", description = "Message API for ChâTop application")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "Send a message to Rental owner", description = "Send a message, userId, rentalId and message are required",
    responses = {
            @ApiResponse(responseCode = "201", description = "Message send with success", content = { @Content(mediaType = "application/json"
                            , schema = @Schema(implementation = MessageResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "message": "Rental created !"
                                    }"""))

            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FiledNotNullOrEmptyException.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://httpstatuses.com/400",
                                        "title": "Bad Request",
                                        "status": 400,
                                        "detail": "Verify your input Data"
                                    }"""))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://httpstatuses.com/401",
                                        "title": "Unauthorized",
                                        "status": 401,
                                        "detail": "Full authentication is required to access this resource"
                                    }"""))
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://httpstatuses.com/403",
                                        "title": "Forbidden",
                                        "status": 403,
                                        "detail": "Access Denied"
                                    }"""))
            })
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Message to send", required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class),
                    examples = @ExampleObject(value = """
                                    {
                                        "owner_id": 1,
                                        "rental_id": 1,
                                        "message": "Hello, I'm interested by your rental"
                                    }""")))
    @PostMapping("/messages")
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody MessageDTO message) {

        if(message.user_id() == null || message.message() == null || message.rental_id() == null) {
            log.error("Message not send, verify your input Data");
            throw new FiledNotNullOrEmptyException("Verify your input Data");
        }
        messageService.sendMessage(message);

        log.info("Message send with success");
        return new ResponseEntity<>(new MessageResponse("Message send with success"), HttpStatusCode.valueOf(201));
    }

}
