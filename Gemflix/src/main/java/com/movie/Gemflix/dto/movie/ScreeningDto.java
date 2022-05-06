package com.movie.Gemflix.dto.movie;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.movie.Gemflix.entity.Movie;
import com.movie.Gemflix.entity.TheaterRoom;
import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningDto {

    @JsonBackReference
    private TheaterRoomDto theaterRoom;
    private MovieDto movie;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime screeningDate;
    private String type;
    @JsonManagedReference
    private TicketDto ticket;

}
