package com.movie.Gemflix.dto.reservation;

import com.movie.Gemflix.entity.TheaterRoom;
import lombok.Data;

import javax.persistence.*;

@Data
public class SeatListDto {

    private Long seId;

    private Long tkId;

    private String seCol;

    private String seRow;
}
