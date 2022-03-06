package com.movie.Gemflix.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "SCREENING")
public class Screening {

    @Id
    @SequenceGenerator(name = "SI_ID_SEQ_GEN", sequenceName = "SI_ID_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SI_ID_SEQ_GEN")
    private Long siId;

    @ManyToOne
    @JoinColumn(name = "ROOM_ID")
    private TheaterRoom theaterRoom;

    @ManyToOne
    @JoinColumn(name = "MV_ID")
    private Movie movie;

    private LocalDateTime StartTime;
    private LocalDateTime endTime;
    private LocalDateTime screeningDate;
    private String type;

}