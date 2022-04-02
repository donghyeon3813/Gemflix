package com.movie.Gemflix.dto.reservation;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScreenSearchDto {
    private Long mvId;
    private Long thId;
    private LocalDateTime date;
}
