package com.movie.Gemflix.repository;

import com.movie.Gemflix.entity.Genre;
import com.movie.Gemflix.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie,Long> {
    Optional<Movie> findByTitle(String title);
}
