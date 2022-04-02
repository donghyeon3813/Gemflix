package com.movie.Gemflix.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private String impUid;
    private String merchantUid;
    private int point;
    private int proAmount;
    private int disAmount;
    private int payAmount;
    private int payType;
    private int disType;
    private String payName;
    private String payPhone;
    private String payAddress;

}