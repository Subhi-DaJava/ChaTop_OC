package com.chatop.dtos;

import java.time.LocalDate;

public record RentalDTO(Integer id, String name, double surface, double price, String picture, String description, Integer owner_id, LocalDate created_at, LocalDate updated_at) {
}
