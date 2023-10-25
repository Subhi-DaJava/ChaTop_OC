package com.chatop.controllers;

import com.chatop.dtos.*;
import com.chatop.exceptions.UnauthorizedUserException;
import com.chatop.services.image_storage.ImageStorageService;
import com.chatop.services.rental.RentalService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RequestMapping("/api")
@Slf4j
@RestController
public class RentalController {

    private final RentalService rentalService;
    private final ImageStorageService imageStorageService;

    public RentalController(RentalService rentalService, ImageStorageService imageStorageService) {
        this.rentalService = rentalService;
        this.imageStorageService = imageStorageService;
    }

    @Value("${image-url}")
    private String imageUrl;


    @GetMapping("/rentals")
    public ResponseEntity<RentalsResponse> retrieveAllRentals() {

        RentalsResponse rentals = rentalService.retrieveAllRentals();
        if (rentals == null) {
            return ResponseEntity.notFound().build();
        }
        log.info("Rentals retrieved successfully");
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/rentals/{id}")
    public ResponseEntity<?> retrieveRentalById(@PathVariable Integer id) {

        RentalDTO rental = rentalService.retrieveRentalById(id);

        if(rental == null) {
            return ResponseEntity.notFound().build();
        }
        log.info("Rental retrieved successfully with id:{}", id);
        return ResponseEntity.ok(rental);
    }

    @PutMapping("/rentals/{id}")
    public ResponseEntity<MessageResponse> updateRentalById(
            @PathVariable Integer id,
            @ModelAttribute("rental") RentalRequest rentalRequest) throws UnauthorizedUserException {

        MessageResponse messageResponse = rentalService.updateRental(id, rentalRequest);

        if(messageResponse == null) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(401));
        }
        log.info("Rental updated successfully with id:{}", id);
        return new ResponseEntity<>(messageResponse, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/rentals")
    public ResponseEntity<MessageResponse> createRental(
            @ModelAttribute("rental") RentalRequest rentalRequest) throws UnauthorizedUserException, IOException {

        String pictureLocation = imageUrl + imageStorageService.savePicture(rentalRequest.getPicture());

        RentalRequestDTO rentalRequestDTO = RentalRequestDTO.builder()
                .name(rentalRequest.getName())
                .surface(rentalRequest.getSurface())
                .price(rentalRequest.getPrice())
                .picture(pictureLocation)
                .description(rentalRequest.getDescription())
                .build();
        rentalRequestDTO.setPicture(pictureLocation);

        MessageResponse messageResponse = rentalService.createRental(rentalRequestDTO);

        if(messageResponse == null) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(401));
        }

        if (messageResponse.equals(new MessageResponse("Field are missing"))) {
            return new ResponseEntity<>(messageResponse, HttpStatusCode.valueOf(400));
        }


        log.info("Rental created successfully");
        return new ResponseEntity<>(messageResponse, HttpStatusCode.valueOf(200));
    }


}
