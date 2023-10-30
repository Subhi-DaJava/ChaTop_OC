package com.chatop.services.rental;

import com.chatop.dtos.*;
import com.chatop.exceptions.RentalNotFondException;
import com.chatop.models.Rental;
import com.chatop.models.User;
import com.chatop.repositories.RentalRepository;
import com.chatop.repositories.UserRepository;
import com.chatop.services.image_storage.ImageStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final ImageStorageService imageStorageService;

    @Value("${image-url}")
    private String imageUrl;


    public RentalServiceImpl(RentalRepository rentalRepository, UserRepository userRepository, ImageStorageService imageStorageService) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.imageStorageService = imageStorageService;
    }

    @Override
    public RentalsResponse retrieveAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();

        if(rentals.isEmpty()) {
            throw new RentalNotFondException("Rentals not found");
        }

        final List<RentalDTO> rentalDTOS = fromRentalToRentalDTOList(rentals);

        RentalsResponse rentalsResponse = new RentalsResponse(rentalDTOS);
        log.info("Rentals retrieved successfully");
        return rentalsResponse;
    }

    @Override
    public RentalDTO retrieveRentalById(Integer id) {
        Rental rental = rentalRepository.findById(id).orElseThrow(
                () -> new RentalNotFondException("Rental not found with id:{%s}".formatted(id)));

        final RentalDTO rentalDTO = buildRentalDTOFromRental(rental);

        log.info("Rental retrieved successfully with id:{%s}".formatted(id));
        return rentalDTO;
    }

    @Override
    public MessageResponse updateRental(Integer id, RentalRequest rentalRequest) {
        Rental rental = rentalRepository.findById(id).orElseThrow(
                () -> new RentalNotFondException("Rental not found with id:{%s}".formatted(id)));

        setRental(rentalRequest, rental);

        rentalRepository.save(rental);
        log.info("Rental updated successfully with id:{%s}".formatted(id));
        return new MessageResponse("Rental updated !");
    }

    @Override
    public MessageResponse createRental(RentalRequest rentalRequest) throws IOException {

        String pictureLocation = imageUrl + imageStorageService.savePicture(rentalRequest.getPicture());

        final RentalRequestDTO rentalRequestDTO = getRentalRequestDTO(rentalRequest, pictureLocation);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User authUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found in DB!!"));

        final Rental rentalSaved = toRental(rentalRequestDTO, authUser);

        rentalRepository.save(rentalSaved);
        log.info("Rental created successfully");
        return new MessageResponse("Rental created !");
    }

    private static RentalDTO buildRentalDTOFromRental(Rental rental) {
        return new RentalDTO(
                rental.getId(),
                rental.getName(),
                rental.getSurface(),
                rental.getPrice(),
                rental.getPicture(),
                rental.getDescription(),
                rental.getOwner().getId(),
                rental.getCreatedAt(),
                rental.getUpdatedAt());
    }

    private static List<RentalDTO> fromRentalToRentalDTOList(List<Rental> rentals) {
        return rentals.stream().map(rental ->
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
    }

    private static void setRental(RentalRequest rentalRequest, Rental rental) {
        rental.setName(rentalRequest.getName());
        rental.setSurface(rentalRequest.getSurface());
        rental.setPrice(rentalRequest.getPrice());
        rental.setDescription(rentalRequest.getDescription());
        rental.setUpdatedAt(LocalDateTime.now());
    }

    private static Rental toRental(RentalRequestDTO rentalRequestDTO, User authUser) {
        return Rental.builder()
                .name(rentalRequestDTO.getName())
                .surface(rentalRequestDTO.getSurface())
                .price(rentalRequestDTO.getPrice())
                .picture(rentalRequestDTO.getPicture())
                .description(rentalRequestDTO.getDescription())
                .owner(authUser)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static RentalRequestDTO getRentalRequestDTO(RentalRequest rentalRequest, String pictureLocation) {
        RentalRequestDTO rentalRequestDTO = RentalRequestDTO.builder()
                .name(rentalRequest.getName())
                .surface(rentalRequest.getSurface())
                .price(rentalRequest.getPrice())
                .picture(pictureLocation)
                .description(rentalRequest.getDescription())
                .build();
        rentalRequestDTO.setPicture(pictureLocation);
        return rentalRequestDTO;
    }
}
