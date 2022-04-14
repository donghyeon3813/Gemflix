package com.movie.Gemflix.dto.member;

import com.movie.Gemflix.entity.MemberRole;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long mId;
    private String id;
    private String password;
    private String phone;
    private String email;
    private int point;
    private String status;
    private MemberRole authority;
    private String grade;
    private String delStatus;
    private String fromSocial;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

}