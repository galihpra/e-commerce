package com.alta.e_commerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.security.SecureRandom;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products")
@Setter
@Getter
public class Product {

    @Id
    @Column(name = "id")
    private String id;

    private String name;

    private String description;

    private double price;

    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
}

