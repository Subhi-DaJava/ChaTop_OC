package com.chatop.controllers;

import com.chatop.dtos.*;
import com.chatop.exceptions.FiledNotNullOrEmptyException;
import com.chatop.exceptions.InvalidImageFormatException;
import com.chatop.exceptions.RentalNotFondException;
import com.chatop.exceptions.UnauthorizedUserException;
import com.chatop.services.rental.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/api")
@Slf4j
@RestController
@Tag(name = "Rental API", description = "Rentals API for ChâTop application")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @Operation(summary = "Get all rentals", description = "Get all rentals",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All Rentals retrieved successfully",
                            content ={@Content(mediaType = "application/json", schema = @Schema(implementation = RentalsResponse.class),
                                    examples = @ExampleObject(value = """
                                    {
                                    "rentals": [
                                        {
                                            "id": 1,
                                            "name": "Appartement 1",
                                            "surface": 100,
                                            "price": 900,
                                            "picture": "https://chatop.com/images/1",
                                            "description": "Appartement 1"
                                        },
                                        {
                                            "id": 2,
                                            "name": "Appartement 2",
                                            "surface": 200,
                                            "price": 2000,
                                            "picture": "https://chatop.com/images/2",
                                            "description": "Appartement 2"
                                        }
                                    ]
                                    }
                                    """, summary = "Rentals retrieved successfully"))
                            }),
                    @ApiResponse(responseCode = "404", description = "Rentals not found",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RentalNotFondException.class),
                                    examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "message": "Rentals not found",
                                      "path": "/api/rentals"
                                    }""", summary = "Rentals not found"))
                            }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content ={@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class),
                                    examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "Unauthorized User, user not logged in",
                                      "path": "/api/rentals"
                                    }""", summary = "Unauthorized User"))}
                    ),
            })
    @GetMapping("/rentals")
    public ResponseEntity<RentalsResponse> retrieveAllRentals() {

        RentalsResponse rentals = rentalService.retrieveAllRentals();

        log.info("Rentals retrieved successfully");
        return ResponseEntity.ok(rentals);
    }

    @Operation(summary = "Get a rental by its id", description = "Get a rental by its id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rental retrieved successfully",
                            content ={@Content(mediaType = "application/json", schema = @Schema(implementation = RentalDTO.class),
                                    examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "name": "Appartement 1",
                                      "surface": 100,
                                      "price": 900,
                                      "picture": "https://chatop.com/images/1",
                                      "description": "Appartement 1"
                                    }
                                    """, summary = "Rental retrieved successfully"))
                            }),
                    @ApiResponse(responseCode = "404", description = "Rental not found",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RentalNotFondException.class),
                                    examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "message": "Rental not found",
                                      "path": "/api/rentals/1"
                                    }""", summary = "Rental not found"))
                            }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content ={@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class),
                                    examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "Unauthorized User, user not logged in",
                                      "path": "/api/rentals/1"
                                    }""", summary = "Unauthorized User"))}
                    ),
            })
    @Parameters(@Parameter(name = "id",
            description = "Id of Rental to be searched", example = "1", required = true,
            schema = @Schema(type = "integer"), content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "1"))))
    @GetMapping("/rentals/{id}")
    public ResponseEntity<RentalDTO> retrieveRentalById(@PathVariable Integer id) {

        RentalDTO rental = rentalService.retrieveRentalById(id);

        log.info("Rental retrieved successfully with id:{}", id);
        return ResponseEntity.ok(rental);
    }

    @Operation(summary = "Update a rental by its id", description = "Update a rental by its id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rental updated by its id",
                            content ={ @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class),
                                    examples = @ExampleObject(value = """
                                    {
                                        "message": "Rental updated !"
                                        }
                                        """, summary = "Rental updated successfully"))
                            }),
                    @ApiResponse(responseCode = "404", description = "Rental not found by given id",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RentalNotFondException.class),
                                    examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "message": "Rental not found with id:1",
                                      "path": "/api/rentals/1"
                                    }""", summary = "Rental not found"))
                            }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content ={@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class),
                                    examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "Unauthorized User, user not logged in",
                                      "path": "/api/rentals/1"
                                    }""", summary = "Unauthorized User"))})

            })
    @Parameters(@Parameter(name = "id", description = "Id of Rental to be updated", example = "1", required = true,
            schema = @Schema(type = "integer"), content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "1"))))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Rental object that needs to be updated", required = true,
            content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = RentalRequest.class)))
    @PutMapping("/rentals/{id}")
    public ResponseEntity<MessageResponse> updateRentalById(
            @PathVariable Integer id,
            @ModelAttribute("rental") RentalRequest rentalRequest) throws UnauthorizedUserException {

        MessageResponse messageResponse = rentalService.updateRental(id, rentalRequest);

        log.info("Rental updated successfully with id:{}", id);
        return new ResponseEntity<>(messageResponse, HttpStatusCode.valueOf(200));
    }

    @Operation(summary = "Create a new rental", description = "Create a new rental, image is required",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Rental created successfully",
                            content ={ @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class),
                                    examples = @ExampleObject(value = """
                                    {
                                        "message": "Rental created successfully"
                                        }
                                        """, summary = "Rental created successfully"))
                            }),
                    @ApiResponse(responseCode = "400", description = "Fields are missing",
                            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = FiledNotNullOrEmptyException.class),
                                    examples = @ExampleObject(value = """
                                    {
                                        "timestamp": "2023-08-31T12:00:00.000+00:00",
                                        "status": 400,
                                        "error": "Bad Request",
                                        "message": "Fields cannot be null or empty",
                                        "path": "/api/rentals"
                                    }""", summary = "Fields are missing"))
                            }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content ={ @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class),
                                    examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "Unauthorized User, user not logged in",
                                      "path": "/api/rentals"
                                    }""", summary = "Unauthorized User"))
                    }),
                    @ApiResponse(responseCode = "403", description = "Access Denied",
                                    content ={@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class),
                                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                                      "status": 403,
                                      "error": "Forbidden",
                                      "message": "Access Denied",
                                      "path": "/api/rentals"
                                    }""", summary = "Access Denied"))
                                    }),
                    @ApiResponse(responseCode = "405", description = "Invalid image format",
                            content ={@Content(mediaType = "application/json", schema = @Schema(implementation = InvalidImageFormatException.class),
                                    examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2023-08-31T12:00:00.000+00:00",
                                      "status": 405,
                                      "error": "Invalid image format",
                                      "message": "Image format must be jpg, jpeg or png",
                                      "path": "/api/rentals"
                                    }""", summary = "Image format must be jpg, jpeg or png"))
                            })
            })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Rental object that needs to be created", required = true,
            content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = RentalRequest.class))
    )
    @PostMapping("/rentals")
    public ResponseEntity<MessageResponse> createRental(
            @ModelAttribute("rental") RentalRequest rentalRequest) throws UnauthorizedUserException, IOException {
        String contentType = rentalRequest.getPicture().getContentType();

        if (contentType == null || contentType.isEmpty()) {
            log.error("The content type of the image is unknown.");
            throw new InvalidImageFormatException("The content type of the image is unknown.");
        }

        if (!(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/jpg"))) {
            log.error("Only images in JPG, PNG or JPEG format are accepted.");
            throw new InvalidImageFormatException("Only images in JPG, PNG or JPEG format are accepted.");
        }

        if(rentalRequest.getName() == null ||
                rentalRequest.getName().isEmpty() ||
                rentalRequest.getSurface() == 0 ||
                rentalRequest.getPrice() == 0 ||
                rentalRequest.getDescription() == null ||
                rentalRequest.getDescription().isEmpty() ||
                rentalRequest.getPicture() == null ) {
            log.error("Fields cannot be null or empty");
            throw new FiledNotNullOrEmptyException("Fields cannot be null or empty");
        }
        MessageResponse messageResponse = rentalService.createRental(rentalRequest);

        log.info("Rental created successfully");
        return new ResponseEntity<>(messageResponse, HttpStatusCode.valueOf(201));
    }


}
