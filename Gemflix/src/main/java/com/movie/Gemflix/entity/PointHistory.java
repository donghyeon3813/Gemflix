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
    @SequenceGenerator(name = "PH_ID_SEQ_GEN", sequenceName = "PH_ID_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PH_ID_SEQ_GEN")
    private Long phId;

    private int changePoint;
    private int beforePoint;
    private int afterPoint;
    private String type;
    private LocalDateTime regDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "M_ID")
    private Member member;

}
