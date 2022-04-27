package com.movie.Gemflix.dto.payment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.movie.Gemflix.dto.member.MemberDto;
import com.movie.Gemflix.entity.PaidProduct;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString(exclude = "paidProducts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private long pmId;
    private String impUid;
    private String merchantUid;
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

    private MemberDto member;

    @JsonManagedReference
    private List<PaidProductDto> paidProducts = new ArrayList<>();

}