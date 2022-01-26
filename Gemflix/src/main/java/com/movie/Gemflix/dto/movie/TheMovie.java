package com.movie.Gemflix.dto.movie;

import lombok.Data;

import java.util.List;

@Data
public class TheMovie {
    private boolean adult; // 성인영화여부
    private String genreName; // 포함장르
    private String id; // 영화 고유번호
    private String title; // 한글설정한 영화 제목
    private String backdrop_path; // 영화 백그라운드 이미지 주소
    private String poster_path; //영화 고유이미지 주소
    private String release_date; //개봉일
    private String overview; // 줄거리
    private String rating;
    private String runtime; //사영시간

}
