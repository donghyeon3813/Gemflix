package com.movie.Gemflix.repository;

import com.movie.Gemflix.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatusIsOrderByCategoryAscRegDateDesc(String status);

}