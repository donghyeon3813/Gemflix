package com.movie.Gemflix.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "theater")
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "THEATER_ROOM")
public class TheaterRoom {

    @Id
    @Column(name = "ROOM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    private String name;
    private int seatCnt;

    @ManyToOne
    @JoinColumn(name = "TH_ID")
    private Theater theater;

}