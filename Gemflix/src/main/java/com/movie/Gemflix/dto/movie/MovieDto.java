package com.movie.Gemflix.dto.movie;

import com.movie.Gemflix.entity.Genre;
import lombok.*;

import java.util.Date;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

    private Long mvId; // 영화 고유번호
    private GenreDto genre; // 장르 고유번호
    private String title; // 영화 제목
    private String rating; // 영화 등급
    private Date openDt; //개봉일
    private String imgUrl; //메인 이미지 경로
    private String content; // 영화 줄거리
    private int accumulatedCnt; //영화 누적 관람객
    private int extent; //영화 상영길이
    private String backImgUrl;
    private String apiId;
    private String status;

}
