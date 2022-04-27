package com.movie.Gemflix.repository.payment;

import com.movie.Gemflix.entity.Payment;
import com.movie.Gemflix.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}