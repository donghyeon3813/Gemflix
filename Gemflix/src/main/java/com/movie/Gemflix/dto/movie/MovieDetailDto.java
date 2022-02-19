package com.movie.Gemflix.dto.movie;

import com.movie.Gemflix.entity.Trailer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class MovieDetailDto {

    private Long mvId;
    private String grNm;
    private String title;
    private String content;
    private int extent;
    private String imgUrl;
    private String backImgUrl;
    private Date openDt;
    private List<String> trailerList;

}
