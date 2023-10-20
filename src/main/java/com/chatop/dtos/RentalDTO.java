package com.chatop.dtos;

import java.time.LocalDateTime;

public record RentalDTO(Integer id, String name, double surface, double price, String picture, String description, Integer owner_id, LocalDateTime created_at, LocalDateTime updated_at) {
}
