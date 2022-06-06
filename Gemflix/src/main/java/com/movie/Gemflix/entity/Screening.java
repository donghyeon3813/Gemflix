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
@ToString(exclude = {"theaterRoom", "movie"})
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "SCREENING")
public class Screening {

    @Id
    @Column(name = "SI_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long siId;

    @ManyToOne
    @JoinColumn(name = "ROOM_ID")
    private TheaterRoom theaterRoom;

    @ManyToOne
    @JoinColumn(name = "MV_ID")
    private Movie movie;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "SCREENING_DATE")
    private LocalDateTime screeningDate;

    @Column(name = "TYPE")
    private String type;

}
