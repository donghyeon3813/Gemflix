package com.movie.Gemflix.dto.payment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.movie.Gemflix.dto.member.MemberDto;
import com.movie.Gemflix.dto.movie.TicketDto;
import com.movie.Gemflix.entity.PaidProduct;
import com.movie.Gemflix.entity.PhotoTicket;
import com.movie.Gemflix.entity.Ticket;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString(exclude = {"paidProducts", "tickets", "member", "photoTicket"})
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

    @JsonManagedReference
    private List<TicketDto> tickets = new ArrayList<>();

    @JsonManagedReference
    private PhotoTicketDto photoTicket;

}