package com.movie.Gemflix.repository.payment;

import com.movie.Gemflix.entity.PaidProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaidProductRepository extends JpaRepository<PaidProduct, Long> {

}