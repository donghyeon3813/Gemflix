package com.movie.Gemflix.repository.movie;

import com.movie.Gemflix.entity.Filmography;
import com.movie.Gemflix.entity.Movie;
import com.movie.Gemflix.entity.People;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FilmographyRepository extends JpaRepository<Filmography, Long> {
    Optional<Filmography> findByMovieAndPeople(Movie movie, People people);

}
