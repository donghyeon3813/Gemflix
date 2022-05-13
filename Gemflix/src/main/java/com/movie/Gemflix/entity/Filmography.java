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
@Table(name = "FILMOGRAPHY")
public class Filmography {

    @Id
    @Column(name = "FG_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pgId;

    @OneToOne
    @JoinColumn(name = "MV_ID")
    private Movie movie;

    @OneToOne
    @JoinColumn(name = "PE_ID")
    private People people;

}
