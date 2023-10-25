package com.chatop.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RentalRequestDTO {
    private String name;
    private double surface;
    private double price;
    private String picture;
    private String description;
}
