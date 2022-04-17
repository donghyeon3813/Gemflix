package com.movie.Gemflix.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.movie.Gemflix.entity.Seat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScreenInfoDto {
    private Long siId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endTime;
    private int seatCnt;
    private int spareSeatCnt;
    private String rating;
    private Long roomId;
    private List<SeatListDto> seatList;
    private String type;
}
