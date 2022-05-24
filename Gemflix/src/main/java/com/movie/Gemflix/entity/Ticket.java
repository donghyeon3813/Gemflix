package com.movie.Gemflix.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"member", "screening", "seat", "payment"})
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "TICKET")
public class Ticket {

    @Id
    @Column(name = "TK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tkId;

    private int price;
    private String rvUseState;

    @ManyToOne
    @JoinColumn( name = "M_ID")
    private Member member;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn( name = "SI_ID")
    private Screening screening;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn (name = "SE_ID")
    private Seat seat;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PM_ID") //결제정보 고유번호
    private Payment payment;

}
