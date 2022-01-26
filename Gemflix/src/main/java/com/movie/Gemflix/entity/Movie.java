package com.movie.Gemflix.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Table(name = "MOVIE")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@SequenceGenerator(
        name = "MV_ID_SEQ_GEN", // 시퀀스 제네레이터 이름
        sequenceName = "MV_ID_SEQ", // 시퀀스 이름
        initialValue = 1, // 시작값
        allocationSize = 1 // 메모리를 통해 할당할 사이즈
)
public class Movie {

    @Id
    @Column(name = "MV_ID")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MV_ID_SEQ_GEN"
    )
    private Long mvId; // 영화 고유번호

    @OneToOne
    @JoinColumn(name = "GR_ID")
    private Genre genre; // 장르 고유번호

    @Column(name = "TITLE")
    private String title; // 영화 제목

    @Column(name = "RATING")
    private String rating; // 영화 등급

    @Column(name = "OPEN_DATE")
    private Date openDt; //개봉일

    @Column(name = "IMG_URL")
    private String imgUrl; //메인 이미지 경로

    @Column(name = "CONTENT")
    private String content; // 영화 줄거리

    @Column(name = "ACCUMULATED_CNT")
    private int accumulatedCnt; //영화 누적 관람객

    @Column(name = "EXTENT")
    private int extent; //영화 상영길이

    @Column(name = "BACK_IMG_URL")
    private String backImgUrl;

    @Column(name = "API_ID")
    private String apiId;

}
