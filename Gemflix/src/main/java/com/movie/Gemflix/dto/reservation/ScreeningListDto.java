package com.movie.Gemflix.dto.reservation;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ScreeningListDto {
    private Long siId;
    @DateTimeFormat(pattern="yyyyMMdd")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern="yyyyMMdd")
    private LocalDateTime endTime;
    private Long mvId;
    private String type;
    private String mvTitle;
    private String trName;
    private int seatCnt;

}
