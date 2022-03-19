package com.movie.Gemflix.repository.movie;

import com.movie.Gemflix.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie,Long> {
    Optional<Movie> findByTitle(String title);
    Optional<List<Movie>> findByStatus(String status);
}
