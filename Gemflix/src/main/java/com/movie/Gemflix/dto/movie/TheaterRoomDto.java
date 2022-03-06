package com.movie.Gemflix.dto.movie;

import com.movie.Gemflix.entity.Theater;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TheaterRoomDto {

    private Long roomId;
    private String name;
    private String seatCnt;

}
