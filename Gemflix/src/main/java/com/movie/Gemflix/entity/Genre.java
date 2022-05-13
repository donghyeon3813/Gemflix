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
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gr_id")
    private Integer grId;
    
    @Column(name = "gr_nm")
    private String grNm;

}
