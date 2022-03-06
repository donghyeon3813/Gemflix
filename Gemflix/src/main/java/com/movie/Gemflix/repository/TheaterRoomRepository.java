package com.movie.Gemflix.repository;

import com.movie.Gemflix.entity.Theater;
import com.movie.Gemflix.entity.TheaterRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRoomRepository extends JpaRepository<TheaterRoom, Long> {

}