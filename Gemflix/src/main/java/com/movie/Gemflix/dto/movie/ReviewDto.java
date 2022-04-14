package com.movie.Gemflix.dto.movie;

import com.movie.Gemflix.entity.Ticket;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private String comment;

    private Long mvId;

    private Float score;

    private Long rvId;

}
