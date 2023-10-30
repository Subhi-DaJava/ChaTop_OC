package com.chatop.controllers;

import com.chatop.authservices.user_auth_service.UserAuthService;
import com.chatop.dtos.AuthRequest;
import com.chatop.dtos.Token;
import com.chatop.exceptions.FiledNotNullOrEmptyException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
@Tag(name = "Login API", description = "Login API for ChâTop application")
@SecurityRequirement(name = "noAuth")
public class LoginController {

    private final UserAuthService userAuthService;

    public LoginController(UserAuthService userAuthService) {

        this.userAuthService = userAuthService;
    }

    @Operation(summary = "User Authentication, User Sign In", description = "Authenticate the user and return a JWT token if the user is valid.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Token.class),
                                    examples = @ExampleObject(value = """
                                    {
                                    "token" : "Valid JWT token"
                                    }
                                    """, summary = "User Authentication Example"))
                            }),

                    @ApiResponse(responseCode = "401", description = "Bad Credentials",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "Incorrect credentials !!",
                                      "path": "/api/auth/login"
                                    }""", summary = "Bad Credentials Example"))),

                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = FiledNotNullOrEmptyException.class),
                                    examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "message": "Fields should not be empty!!",
                                      "path": "/api/auth/login"
                                    }""", summary = "Bad Request Example"))
                            })
            })
    @Parameters(@Parameter(name = "AuthRequest", description = "User credentials for authentication"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            {
              "email": "test@test.com",
              "password": "test!31"
            }""", summary = "User Authentication Example"))
    )
    @PostMapping(value = "/auth/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Token> login(@RequestBody AuthRequest authRequest) {
        if(authRequest.email() == null || authRequest.password() == null || authRequest.email().isEmpty() || authRequest.password().isEmpty()) {
            log.error("Fields should not be empty !!");
            throw new FiledNotNullOrEmptyException("Fields should not be empty!!");
        }
        Token token = userAuthService.login(authRequest);
        log.info("User with email {} authenticated successfully", authRequest.email());
        return ResponseEntity.ok(token);
    }

}
