package com.example.Kiosk.item;

import com.example.Kiosk.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByProduct(Product product);
}
