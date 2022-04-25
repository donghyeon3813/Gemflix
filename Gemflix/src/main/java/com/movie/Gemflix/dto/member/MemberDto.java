package com.movie.Gemflix.dto.member;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.movie.Gemflix.entity.MemberRole;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    @NotBlank
    @Size(min = 8, max = 30, message = "아이디는 8~30자 이내로 입력해주세요.")
    private String id;

    @NotBlank
    @Size(min = 8, max = 30, message = "비밀번호는 8~30자 이내로 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$",
            message = "비밀번호는 최소 하나의 문자, 하나의 숫자, 하나의 특수문자를 포함해 입력해주세요.")
    private String password;

    @NotBlank
    @Pattern(regexp = "^\\d{2,3}\\d{3,4}\\d{4}$",
            message = "핸드폰번호 형식에 맞게 입력해주세요.")
    private String phone;

    @NotBlank
    @Email(message = "이메일 형식에 맞게 입력해주세요. xxx@xxx.xxx")
    private String email;

    //그 외 기본 정보들
    private Long mId;
    private int point; //회원가입 포인트
    private String status;
    private MemberRole authority;
    private String grade;
    private String delStatus;
    private String fromSocial;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @JsonManagedReference
    private List<PointHistoryDto> pointHistories = new ArrayList<>();

    public void addPointHistory(PointHistoryDto pointHistoryDto){
        pointHistories.add(pointHistoryDto);
    }

}