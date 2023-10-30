package com.chatop.controllers;

import com.chatop.authservices.user_auth_service.UserAuthService;
import com.chatop.dtos.ErrorMessage;
import com.chatop.dtos.MeResponse;
import com.chatop.dtos.Token;
import com.chatop.dtos.SignUpRequest;
import com.chatop.exceptions.UserAlreadyExistsException;
import com.chatop.exceptions.UserNotFoundException;
import com.chatop.models.User;
import com.chatop.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api")
@Tag(name = "User API", description = "The User API. Contains all the operations that can be performed on a user in ChâTop application.")
public class UserController {

    private final UserRepository userRepository;
    private final UserAuthService userAuthService;

    @Operation(summary = "Post endpoint for register a user", description = "Register a user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User registered successfully",
                            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Token.class),
                                    examples = @ExampleObject(value = """
                    {
                    "token" : "Valid JWT token"
                    }
                    """, summary = "User Registration Example, successful registration"))
                            }),

                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class),
                                    examples = @ExampleObject(value = """
                    {
                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                      "status": 400,
                      "error": "Bad Request",
                      "message": "Fields should not be empty !!",
                      "path": "/api/auth/register"
                    }
                    """, summary = "Bad Request Example"))
                            }),

                    @ApiResponse(responseCode = "409", description = "User already exists",
                            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserAlreadyExistsException.class),
                                    examples = @ExampleObject(value = """
                    {
                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                      "status": 409,
                      "error": "Bad Request",
                      "message": "User with this email already exists in DB!!",
                      "path": "/api/auth/register"
                    }
                    """, summary = "User already exists Example"))
                            })

            })
    @Parameters(@Parameter(name = "SignUpRequest", description = "New User sign up DTO"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SignUpRequest.class),
            examples = @ExampleObject(value = """
                   {
                     "email": "test@test.com",
                     "password": "test!31",
                     "name": "test Test"
                   }""", summary = "User Registration Example"))})

    @SecurityRequirement(name = "noAuth")
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody SignUpRequest signUpRequest) throws UserAlreadyExistsException {

        if(signUpRequest.name() == null || signUpRequest.email() == null || signUpRequest.password() == null) {
            return new ResponseEntity<>(new ErrorMessage("Fields should not be empty!!"), HttpStatus.BAD_REQUEST);
        }

        Token token = userAuthService.register(signUpRequest);
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }


    @Operation(summary = "Get endpoint for retrieving a user", description = "Retrieve a user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MeResponse.class),
                                    examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "name": "test Test",
                                      "email": "test@test.com",
                                      "createdAt": "2023-10-23T12:00:00.000+00:00",
                                      "updatedAt": "2023-10-23T12:00:00.000+00:00"
                                      }""", summary = "User retrieved successfully Example"))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class),
                                    examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "User should be authenticated!!",
                                      "path": "/api/auth/me"
                                    }""", summary = "Unauthorized Example"))
                            }),
                    @ApiResponse(responseCode = "404", description = "Not Found",
                            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserNotFoundException.class),
                                    examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "message": "User not found with this email"
                                     }""" , summary = "User not found Example"))
                            }),
                    @ApiResponse(responseCode = "403", description = "Forbidden",
                            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class),
                                    examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                                      "status": 403,
                                      "error": "Forbidden",
                                      "message": "Access Denied"
                                     }""" , summary = "Access Denied Example"))
                            })
            })
    @Parameters(@Parameter(name = "MeResponse", description = "User profile DTO"))
    @GetMapping("/auth/me")
    public ResponseEntity<?> retrieveUser() {

        MeResponse meResponse = userAuthService.retrieveProfile();

        if (meResponse == null) {
            return new ResponseEntity<>(new ErrorMessage("User should be authenticated!!"), HttpStatus.UNAUTHORIZED);
        }
        log.info("User retrieved successfully");
        return ResponseEntity.ok(meResponse);
    }


    @Operation(summary = "Get endpoint for retrieving a user by its id", description = "Retrieve a user by its id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MeResponse.class),
                                    examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "name": "Owner Name",
                                      "email": "test@test.com",
                                      "createdAt": "2023-10-23T12:00:00.000+00:00",
                                      "updatedAt": "2023-10-23T12:00:00.000+00:00"
                                      }""", summary = "User retrieved successfully Example"))
                    }),
                    @ApiResponse(responseCode = "404", description = "Not Found",
                            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserNotFoundException.class),
                                    examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "message": "User not found with this id"
                                     }""" , summary = "User not found Example"))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class),
                                    examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "User should be authenticated!!",
                                      "path": "/api/user/{id}"
                                    }""", summary = "Unauthorized Example"))
                            }),
                    @ApiResponse(responseCode = "403", description = "Forbidden",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                                      "status": 403,
                                      "error": "Forbidden",
                                      "message": "Access Denied"
                                     }""" , summary = "Access Denied Example")))
            })
    @Parameters(@Parameter(name = "id", description = "User id", example = "1", required = true, schema = @Schema(type = "integer"), content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "1"))))
    @GetMapping("/user/{id}")
    public ResponseEntity<MeResponse> retrieveUserById(@PathVariable Integer id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new UserNotFoundException("User not found with this id:{%d}".formatted(id));
        }
        MeResponse meResponse =
                new MeResponse(user.get().getId(), user.get().getName(), user.get().getEmail(), user.get().getCreatedAt(), user.get().getUpdatedAt());
        return ResponseEntity.ok(meResponse);
    }
}
