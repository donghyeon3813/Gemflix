package com.movie.Gemflix.repository;

import com.movie.Gemflix.entity.Trailer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrailerRepository extends JpaRepository<Trailer, Long> {
    public Optional<Trailer> findByTrLocation(String trLocation);
}
