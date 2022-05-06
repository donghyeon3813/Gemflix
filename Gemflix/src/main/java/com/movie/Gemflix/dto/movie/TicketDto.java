package com.movie.Gemflix.dto.movie;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.movie.Gemflix.dto.member.MemberDto;
import com.movie.Gemflix.dto.payment.PaymentDto;
import com.movie.Gemflix.dto.reservation.SeatDto;
import com.movie.Gemflix.entity.Screening;
import com.movie.Gemflix.entity.Seat;
import lombok.*;

@Data
@ToString(exclude = {"member", "screening", "payment", "seat"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {
    private long tkId;
    private int price;
    private String rvUseState;

    private MemberDto member;
    private ScreeningDto screening;
    private SeatDto seat;
    @JsonBackReference
    private PaymentDto payment;

    @JsonManagedReference
    private ReviewDto review;

}
