package com.movie.Gemflix.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "THEATER")
public class Theater {

    @Id
    @Column(name = "TH_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long thId;

    @Column(name = "PLACE")
    private String place;

    @Column(name = "LOCATION")
    private String location;

}