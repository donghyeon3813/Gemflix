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
@ToString(exclude = "member")
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "POINT_HISTORY")
public class PointHistory {

    @Id
    @Column(name = "PH_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phId;

    @Column(name = "CHANGE_POINT")
    private int changePoint;

    @Column(name = "BEFORE_POINT")
    private int beforePoint;

    @Column(name = "AFTER_POINT")
    private int afterPoint;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "REG_DATE")
    private LocalDateTime regDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "M_ID")
    private Member member;

}
