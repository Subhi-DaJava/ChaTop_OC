package com.chatop.controllers;

import com.chatop.dtos.MessageResponse;
import com.chatop.dtos.RentalDTO;
import com.chatop.dtos.RentalsResponse;
import com.chatop.models.Rental;
import com.chatop.repositories.RentalRepository;
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
public class RentalController {

    private final RentalRepository rentalRepository;

    @GetMapping("/rentals")
    public ResponseEntity<RentalsResponse> retrieveAllRentals() {

        List<Rental> rentals = rentalRepository.findAll();

        if(rentals.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<RentalDTO> rentalDTOS = rentals.stream().map(rental ->
                new RentalDTO(
                        rental.getId(),
                        rental.getName(),
                        rental.getSurface(),
                        rental.getPrice(),
                        rental.getPicture(),
                        rental.getDescription(),
                        rental.getOwner().getId(),
                        rental.getCreatedAt(),
                        rental.getUpdatedAt())).toList();

        RentalsResponse rentalsResponse = new RentalsResponse("Rentals", rentalDTOS);
        return ResponseEntity.ok(rentalsResponse);
    }

    @GetMapping("/rentals/{id}")
    public ResponseEntity<?> retrieveRentalById(@PathVariable Integer id) {
        Rental rental = rentalRepository.findById(id).orElse(null);
        if(rental == null) {
            return ResponseEntity.notFound().build();
        }
        RentalDTO rentalDTO = new RentalDTO(
                rental.getId(),
                rental.getName(),
                rental.getSurface(),
                rental.getPrice(),
                rental.getPicture(),
                rental.getDescription(),
                rental.getOwner().getId(),
                rental.getCreatedAt(),
                rental.getUpdatedAt());
        return ResponseEntity.ok(rentalDTO);
    }

    @PutMapping("/rentals/{id}")
    public ResponseEntity<MessageResponse> updateRentalById(@PathVariable Integer id) {
        Rental rental = rentalRepository.findById(id).orElse(null);

        if(rental == null) {
            return ResponseEntity.notFound().build();
        }
        rentalRepository.save(rental);
        return new ResponseEntity<>(new MessageResponse("Rental updated !"), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/rentals")
    public ResponseEntity<MessageResponse> createRental(@RequestBody Rental rental) {
        rentalRepository.save(rental);
        return new ResponseEntity<>(new MessageResponse("Rental created !"), HttpStatusCode.valueOf(200));
    }

}
