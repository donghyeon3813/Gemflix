package com.movie.Gemflix.dto.movie;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

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

}
