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
    @SequenceGenerator(name = "PT_ID_SEQ_GEN", sequenceName = "PT_ID_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PT_ID_SEQ_GEN")
    private Long ptId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PM_ID") //결제정보 고유번호
    private Payment payment;

    private int price;

    private int cnt;

}
