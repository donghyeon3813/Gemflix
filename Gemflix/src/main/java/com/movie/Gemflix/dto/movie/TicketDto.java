package com.movie.Gemflix.dto.movie;

import com.movie.Gemflix.dto.member.MemberDto;
import com.movie.Gemflix.dto.payment.PaymentDto;
import com.movie.Gemflix.entity.Screening;
import com.movie.Gemflix.entity.Seat;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {

    private int price;
    private String rvUseState;
    private MemberDto member;
    private Screening screening;
    private Seat seat;
    private PaymentDto payment;

}
