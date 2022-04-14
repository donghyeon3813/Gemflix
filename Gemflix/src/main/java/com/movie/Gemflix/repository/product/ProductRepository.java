package com.movie.Gemflix.repository.product;

import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByPrIdAndDelStatus(long prId, String status);

}