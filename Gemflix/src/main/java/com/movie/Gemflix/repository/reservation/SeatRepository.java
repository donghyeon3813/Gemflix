package com.movie.Gemflix.repository.reservation;

import com.movie.Gemflix.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
