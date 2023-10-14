package com.chatop.controllers;

import com.chatop.models.Rental;
import com.chatop.repositories.RentalRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Slf4j
public class RentalController {

    private final RentalRepository rentalRepository;

    @GetMapping("/rentals")
    public ResponseEntity<List<Rental>> retrieveAllRentals() {
        return ResponseEntity.ok(rentalRepository.findAll());
    }
}
