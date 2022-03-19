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
@ToString
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "THEATER_ROOM")
public class TheaterRoom {

    @Id
    @SequenceGenerator(name = "ROOM_ID_SEQ_GEN", sequenceName = "ROOM_ID_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROOM_ID_SEQ_GEN")
    private Long roomId;

    private String name;
    private String seatCnt;

    @ManyToOne
    @JoinColumn(name = "TH_ID")
    private Theater theater;

}