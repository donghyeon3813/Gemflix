package com.movie.Gemflix.repository;


import com.movie.Gemflix.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductRepositorySupport {

    private final JPAQueryFactory query;
    private QProduct product = QProduct.product;
    private QCategory category = QCategory.category;

    public List<Product> findByStatusProductAndCategory(String status) throws Exception{
        List<Product> products = query.select(product)
                .from(product)
                .leftJoin(product.category, category)
                .on(product.status.eq(status))
                .orderBy(category.cgId.asc())
                .orderBy(product.regDate.desc())
                .fetch();
        System.out.println("products: " + products);
        return products;
    }

}