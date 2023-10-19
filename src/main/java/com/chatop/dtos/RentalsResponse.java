package com.chatop.dtos;

import java.util.List;

public record RentalsResponse(String rentals, List<RentalDTO> rentalsDTO) {
}
