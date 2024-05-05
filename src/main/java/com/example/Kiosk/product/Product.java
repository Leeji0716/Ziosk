package com.example.Kiosk.product;

import com.example.Kiosk.category.Category;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;


@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TEXT")
    private String productName;

    private int price;

    private String image;

    private int quantity;

    @ManyToOne
    private Category category;
}
