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
@Table(name = "SEAT")
public class Seat {

    @Id
    @SequenceGenerator(name = "SE_ID_SEQ_GEN", sequenceName = "SE_ID_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SE_ID_SEQ_GEN")
    private Long seId;

    @ManyToOne
    @JoinColumn( name = "ROOM_ID")
    private TheaterRoom theaterRoom;

    private String seCol;

    private String seRow;
}
