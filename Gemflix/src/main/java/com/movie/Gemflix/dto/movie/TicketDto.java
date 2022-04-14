package com.movie.Gemflix.dto.movie;

import com.movie.Gemflix.dto.member.MemberDto;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {

    private String seat;
    private int price;
    private String rvUseState;

    private MemberDto member;
    private ScreeningDto screening;

}
