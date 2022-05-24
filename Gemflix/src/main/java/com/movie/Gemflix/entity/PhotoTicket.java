package com.movie.Gemflix.entity;

import com.movie.Gemflix.dto.payment.PaymentDto;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "PHOTO_TICKET")
public class PhotoTicket {

    @Id
    @Column(name = "PR_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ptId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PM_ID") //결제정보 고유번호
    private Payment payment;

    private int price;

    private int cnt;

}
