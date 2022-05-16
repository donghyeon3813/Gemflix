package com.movie.Gemflix.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Data
@Table(name = "GENRE")
@SequenceGenerator(
        name = "GR_ID_SEQ_GEN",
        sequenceName = "GR_ID_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gr_id")
    private Integer grId;
    
    @Column(name = "gr_nm")
    private String grNm;

}
