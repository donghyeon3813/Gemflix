package com.movie.Gemflix.dto.payment;

import com.movie.Gemflix.entity.Payment;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoTicketDto {
    private PaymentDto payment;
    private int price;
    private int cnt;
}
