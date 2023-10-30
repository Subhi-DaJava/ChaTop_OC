package com.chatop.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalRequest  {
    private String name;
    private double surface;
    private double price;
    private MultipartFile picture;
    private String description;
}
