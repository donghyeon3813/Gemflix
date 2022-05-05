package com.movie.Gemflix.dto.payment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.movie.Gemflix.dto.product.ProductDto;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidProductDto {

    private int count;
    private int price;

    private ProductDto product; //상품

    @JsonBackReference
    private PaymentDto payment; //결제정보

}

