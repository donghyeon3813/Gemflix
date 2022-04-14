package com.movie.Gemflix.dto.reservation;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenSearchDto {
    private Long mvId;
    private Long thId;
    private LocalDateTime date;
}
