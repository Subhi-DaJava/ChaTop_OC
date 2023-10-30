package com.chatop.services.rental;

import com.chatop.dtos.*;

import java.io.IOException;

public interface RentalService {

    RentalsResponse retrieveAllRentals();
    RentalDTO retrieveRentalById(Integer id);
    MessageResponse updateRental(Integer id, RentalRequest rentalRequest);

    MessageResponse createRental(RentalRequest rentalRequest) throws IOException;
}
