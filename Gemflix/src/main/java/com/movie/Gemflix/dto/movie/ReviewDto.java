package com.movie.Gemflix.dto.movie;

import com.movie.Gemflix.entity.Ticket;
import lombok.Data;

@Data
public class ReviewDto {
    private String comment;

    private Long mvId;

    private Float score;

}
