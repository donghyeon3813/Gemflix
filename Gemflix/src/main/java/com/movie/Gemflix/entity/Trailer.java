package com.movie.Gemflix.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "TRAILER")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@SequenceGenerator(
        name = "TR_ID_SEQ_GEN", // 시퀀스 제네레이터 이름
        sequenceName = "TR_ID_SEQ", // 시퀀스 이름
        initialValue = 1, // 시작값
        allocationSize = 1 // 메모리를 통해 할당할 사이즈
)
public class Trailer {
    @Column(name = "TR_ID")
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "TR_ID_SEQ_GEN"
    )
    private Long trId;

    @ManyToOne
    @JoinColumn(name = "MV_ID")
    private Movie movie;

    @Column(name = "TR_LOCATION")
    private String trLocation;

    @Column(name = "IMG_LOCATION")
    private String imgLocation;

}
