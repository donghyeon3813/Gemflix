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
public class Trailer {
    @Column(name = "TR_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trId;

    @ManyToOne
    @JoinColumn(name = "MV_ID")
    private Movie movie;

    @Column(name = "TR_LOCATION")
    private String trLocation;

    @Column(name = "IMG_LOCATION")
    private String imgLocation;

}
