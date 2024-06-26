package com.example.Kiosk.product;

import com.example.Kiosk.category.Category;
import com.example.Kiosk.item.Item;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.List;


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
    @JsonBackReference
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<Item> itemList;

    private int total;
}
