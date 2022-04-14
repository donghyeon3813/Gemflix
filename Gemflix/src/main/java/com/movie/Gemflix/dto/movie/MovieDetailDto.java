package com.movie.Gemflix.dto.movie;

import com.movie.Gemflix.entity.People;
import com.movie.Gemflix.entity.Trailer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetailDto {

    private Long mvId;
    private String grNm;
    private String title;
    private String content;
    private int extent;
    private String imgUrl;
    private String backImgUrl;
    private Date openDt;
    private List<Trailer> trailerList;
    private List<People> peopleList;
    private double score;

}
