package com.movie.Gemflix.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"paidProducts", "tickets", "photoTicket"})
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "PAYMENT")
public class Payment {

    @Id
    @Column(name = "PM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pmId; //결제정보 고유번호

    @ManyToOne
    @JoinColumn(name = "M_ID") //회원 고유번호
    private Member member;

    @Column(name = "POINT")
    private int point;

    @Column(name = "PRO_AMOUNT")
    private int proAmount;

    @Column(name = "DIS_AMOUNT")
    private int disAmount;

    @Column(name = "PAY_AMOUNT")
    private int payAmount;

    @Column(name = "PAY_TYPE")
    private String payType;

    @Column(name = "DIS_TYPE")
    private String disType;

    @Column(name = "PAY_DATE")
    private LocalDateTime payDate;

    @Column(name = "PAY_STATUS")
    private String payStatus;

    @Column(name = "PAY_NAME")
    private String payName;

    @Column(name = "PAY_PHONE")
    private String payPhone;

    @Column(name = "PAY_ADDRESS")
    private String payAddress;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<PaidProduct> paidProducts = new ArrayList<>();

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private PhotoTicket photoTicket;
}