package com.chatop.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RentalRequest  {
    private String name;
    private double surface;
    private double price;
    private MultipartFile picture;
    private String description;
}
