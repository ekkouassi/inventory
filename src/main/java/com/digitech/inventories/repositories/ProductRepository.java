package com.digitech.inventories.repositories;

import com.digitech.inventories.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
