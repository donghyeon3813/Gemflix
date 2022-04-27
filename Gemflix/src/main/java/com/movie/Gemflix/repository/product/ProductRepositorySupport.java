package com.movie.Gemflix.repository.product;


import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductRepositorySupport {

    private final JPAQueryFactory query;
    private QProduct product = QProduct.product;
    private QCategory category = QCategory.category;

    public List<Product> findByStatusAndDelStatusProductAndCategory(String status) throws Exception{
        List<Product> products = query.select(product)
                .from(product)
                .leftJoin(product.category, category)
                .on(product.status.eq(status))
                .where(product.delStatus.eq(Constant.BooleanStringValue.FALSE))
                .orderBy(category.cgId.asc())
                .orderBy(product.regDate.desc())
                .fetch();
        System.out.println("products: " + products);
        return products;
    }

    public void modifyProduct(Product newProduct) {
        if(newProduct.getImgLocation() == null){
            query.update(product)
                    .set(product.name, newProduct.getName())
                    .set(product.content, newProduct.getContent())
                    .set(product.price, newProduct.getPrice())
                    .set(product.status, newProduct.getStatus())
                    .set(product.category.cgId, newProduct.getCategory().getCgId())
                    .set(product.modDate, LocalDateTime.now())
                    .where(product.prId.eq(newProduct.getPrId()))
                    .execute();
        }else{
            query.update(product)
                    .set(product.name, newProduct.getName())
                    .set(product.content, newProduct.getContent())
                    .set(product.price, newProduct.getPrice())
                    .set(product.status, newProduct.getStatus())
                    .set(product.imgLocation, newProduct.getImgLocation())
                    .set(product.category.cgId, newProduct.getCategory().getCgId())
                    .set(product.modDate, LocalDateTime.now())
                    .where(product.prId.eq(newProduct.getPrId()))
                    .execute();
        }

    }

    public void deleteProduct(Long prId) {
        query.update(product)
                .set(product.delStatus, Constant.BooleanStringValue.TRUE)
                .set(product.modDate, LocalDateTime.now())
                .where(product.prId.eq(prId))
                .execute();
    }
}