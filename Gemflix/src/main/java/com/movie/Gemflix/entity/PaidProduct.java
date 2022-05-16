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
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long ppId;

        private int count;
        private int price;

        @ManyToOne
        @JoinColumn(name = "PR_ID") //상품 고유번호
        private Product product;

        @ManyToOne
        @JoinColumn(name = "PM_ID") //결제정보 고유번호
        private Payment payment;

}
