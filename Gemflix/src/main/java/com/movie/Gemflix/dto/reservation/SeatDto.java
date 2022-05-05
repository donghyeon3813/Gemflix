package com.movie.Gemflix.dto.reservation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.movie.Gemflix.dto.movie.TicketDto;
import com.movie.Gemflix.entity.TheaterRoom;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatDto {
    private Long seId;

    @JsonBackReference
    private TheaterRoom theaterRoom;

    private String seCol;

    private String seRow;

    @JsonManagedReference
    private TicketDto ticketDto;
}
