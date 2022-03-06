package com.movie.Gemflix.dto.movie;

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

    private TheaterRoomDto theaterRoom;
    private MovieDto movie;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime screeningDate;
    private String type;

}
