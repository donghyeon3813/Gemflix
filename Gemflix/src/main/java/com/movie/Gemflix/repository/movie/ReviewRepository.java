package com.movie.Gemflix.repository.movie;

import com.movie.Gemflix.entity.Review;
import com.movie.Gemflix.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByTicket(Ticket ticket);
}
