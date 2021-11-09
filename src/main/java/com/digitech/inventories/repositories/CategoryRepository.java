package com.digitech.inventories.repositories;

import com.digitech.inventories.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Ernest KOUASSI
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
