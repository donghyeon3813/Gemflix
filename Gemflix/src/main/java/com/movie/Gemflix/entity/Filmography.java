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
@SequenceGenerator(
        name = "FG_ID_SEQ_GEN",
        sequenceName = "FG_ID_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class Filmography {

    @Id
    @Column(name = "FG_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "FG_ID_SEQ_GEN")
    private Long pgId;

    @OneToOne
    @JoinColumn(name = "MV_ID")
    private Movie movie;

    @OneToOne
    @JoinColumn(name = "PE_ID")
    private People people;

}
