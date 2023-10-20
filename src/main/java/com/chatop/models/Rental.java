package com.chatop.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Entity
@Table(name = "RENTALS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rental {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "surface")
    private double surface;

    @Column(name = "picture")
    private String picture;

    @Column(name = "description", length = 2000)
    private String description;

    @ManyToOne
    @JoinColumn(nullable = false, name = "owner_id")
    private User owner;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime updatedAt;

}
