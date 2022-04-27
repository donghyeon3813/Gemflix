package com.movie.Gemflix.repository.payment;


import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.dto.movie.FilmographyList;
import com.movie.Gemflix.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PaymentRepositorySupport {

    private final JPAQueryFactory query;
    private QPayment payment = QPayment.payment;
    private QPaidProduct paidProduct = QPaidProduct.paidProduct;
    private QProduct product = QProduct.product;


    public List<Payment> findByMIdOrderByPayDateDesc(String memberId) throws Exception{
        List<Payment> payments = query.select(payment).distinct()
                .from(payment)
                .leftJoin(payment.paidProducts, paidProduct)
                .on(payment.pmId.eq(paidProduct.payment.pmId))
                .leftJoin(paidProduct.product, product)
                .on(paidProduct.product.prId.eq(product.prId))
                .where(payment.member.id.eq(memberId))
                .orderBy(payment.payDate.desc())
                .fetch();

        System.out.println("payments: " + payments);
        return payments;
    }

}