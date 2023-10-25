package com.chatop.services.rental;

import com.chatop.dtos.*;

public interface RentalService {

    RentalsResponse retrieveAllRentals();
    RentalDTO retrieveRentalById(Integer id);
    MessageResponse updateRental(Integer id, RentalRequest rentalRequest);

    MessageResponse createRental(RentalRequestDTO rentalRequestDTO);
}
