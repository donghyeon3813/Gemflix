package com.movie.Gemflix.dto.member;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString(exclude = "member")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointHistoryDto {

    private Long phId;
    private int changePoint;
    private int point;
    private String type;
    private LocalDateTime regDate;

    @JsonBackReference
    private RegMemberDto member;

}

