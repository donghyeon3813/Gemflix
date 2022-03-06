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
    @SequenceGenerator(name = "TH_ID_SEQ_GEN", sequenceName = "TH_ID_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TH_ID_SEQ_GEN")
    private Long thId;

    private String place;
    private String location;

}