package com.movie.Gemflix.dto.movie;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRegisterDto {
    private String content;
    private Long mvId;
    private Long rvId;
    private float score;


}
