package com.movie.Gemflix.repository;

import com.movie.Gemflix.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}