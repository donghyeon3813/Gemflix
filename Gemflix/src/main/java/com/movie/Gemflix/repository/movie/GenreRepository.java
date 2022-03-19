package com.movie.Gemflix.repository.movie;

import com.movie.Gemflix.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface GenreRepository extends JpaRepository<Genre,Integer> {
    Optional<Genre> findByGrNm(String grNm);
}
