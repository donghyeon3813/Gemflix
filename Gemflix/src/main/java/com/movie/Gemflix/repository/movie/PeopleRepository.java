package com.movie.Gemflix.repository.movie;

import com.movie.Gemflix.entity.People;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PeopleRepository extends JpaRepository<People, Long> {
    Optional<People> findByApiId(String apiId);
    boolean existsByApiId(String apiId);
}
