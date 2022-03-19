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
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "TICKET")
public class Ticket {

    @Id
    @SequenceGenerator(name = "TK_ID_SEQ_GEN", sequenceName = "TK_ID_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TK_ID_SEQ_GEN")
    private Long tkId;

    @ManyToOne
    @JoinColumn( name = "M_ID")
    private Member member;

    @ManyToOne
    @JoinColumn( name = "SI_ID")
    private Screening screening;

    private String seat;

    private int price;

    private String rvUseState;
}
