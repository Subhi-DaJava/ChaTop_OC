package com.chatop.services.rental;

import com.chatop.dtos.*;
import com.chatop.exceptions.RentalNotFondException;
import com.chatop.models.Rental;
import com.chatop.models.User;
import com.chatop.repositories.RentalRepository;
import com.chatop.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    public RentalServiceImpl(RentalRepository rentalRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RentalsResponse retrieveAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();

        if(rentals.isEmpty()) {
            throw new RentalNotFondException("Rentals not found");
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

        RentalsResponse rentalsResponse = new RentalsResponse(rentalDTOS);
        log.info("Rentals retrieved successfully");
        return rentalsResponse;
    }

    @Override
    public RentalDTO retrieveRentalById(Integer id) {
        Rental rental = rentalRepository.findById(id).orElseThrow(
                () -> new RentalNotFondException("Rental not found with id:{%s}".formatted(id)));

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

        log.info("Rental retrieved successfully with id:{%s}".formatted(id));
        return rentalDTO;
    }

    @Override
    public MessageResponse updateRental(Integer id, RentalRequest rentalRequest) {
        Rental rental = rentalRepository.findById(id).orElseThrow(
                () -> new RentalNotFondException("Rental not found with id:{%s}".formatted(id)));

        rental.setName(rentalRequest.getName());
        rental.setSurface(rentalRequest.getSurface());
        rental.setPrice(rentalRequest.getPrice());
        rental.setDescription(rentalRequest.getDescription());
        rental.setUpdatedAt(LocalDateTime.now());

        rentalRepository.save(rental);

        return new MessageResponse("Rental updated !");
    }

    @Override
    public MessageResponse createRental(RentalRequestDTO rentalRequestDTO) {

        if(rentalRequestDTO.getName() == null || rentalRequestDTO.getName().isEmpty() || rentalRequestDTO.getDescription() == null || rentalRequestDTO.getPicture() == null ) {
            return new MessageResponse("Fields are missing");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return new MessageResponse("User is not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User authUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found in DB!!"));

        Rental rentalSaved = Rental.builder()
                .name(rentalRequestDTO.getName())
                .surface(rentalRequestDTO.getSurface())
                .price(rentalRequestDTO.getPrice())
                .picture(rentalRequestDTO.getPicture())
                .description(rentalRequestDTO.getDescription())
                .owner(authUser)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        rentalRepository.save(rentalSaved);

        return new MessageResponse("Rental created !");
    }
}
