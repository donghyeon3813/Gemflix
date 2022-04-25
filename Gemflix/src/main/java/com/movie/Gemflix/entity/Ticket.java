package com.movie.Gemflix.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"member", "screening"})
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "TICKET")
public class Ticket {

    @Id
    @SequenceGenerator(name = "TK_ID_SEQ_GEN", sequenceName = "TK_ID_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TK_ID_SEQ_GEN")
    private Long tkId;

    private int price;
    private String rvUseState;

    @ManyToOne
    @JoinColumn( name = "M_ID")
    private Member member;

    @ManyToOne
    @JoinColumn( name = "SI_ID")
    private Screening screening;

    @OneToOne
    @JoinColumn (name = "SE_ID")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "PM_ID") //결제정보 고유번호
    private Payment payment;
}
