package com.movie.Gemflix.repository.payment;

import com.movie.Gemflix.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}