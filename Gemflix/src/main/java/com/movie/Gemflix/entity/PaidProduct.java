package com.movie.Gemflix.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"product", "ticket", "payment"})
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "PAID_PRODUCT")
public class PaidProduct {

        @Id
        @SequenceGenerator(name = "PP_ID_SEQ_GEN", sequenceName = "PP_ID_SEQ", initialValue = 1, allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PP_ID_SEQ_GEN")
        private Long ppId;

        private int count;
        private int price;

        @ManyToOne
        @JoinColumn(name = "PR_ID") //상품 고유번호
        private Product product;

        @ManyToOne
        @JoinColumn(name = "TK_ID") //티켓 고유번호
        private Ticket ticket;

        @ManyToOne
        @JoinColumn(name = "PM_ID") //결제정보 고유번호
        private Payment payment;

}
