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
@ToString(exclude = "paidProducts")
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "PAYMENT")
public class Payment {

    @Id
    @SequenceGenerator(name = "PM_ID_SEQ_GEN", sequenceName = "PM_ID_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PM_ID_SEQ_GEN")
    private Long pmId; //결제정보 고유번호

    @ManyToOne
    @JoinColumn(name = "M_ID") //회원 고유번호
    private Member member;

    private int point;
    private int proAmount;
    private int disAmount;
    private int payAmount;
    private String payType;
    private String disType;
    private LocalDateTime payDate;
    private String payStatus;
    private String payName;
    private String payPhone;
    private String payAddress;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<PaidProduct> paidProducts = new ArrayList<>();

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private PhotoTicket photoTicket;
}