package com.movie.Gemflix.dto;

import lombok.*;

import javax.validation.constraints.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    @NotBlank
    @Size(min = 8, max = 30, message = "8~30자 이내로 입력해주세요.")
    private String id;

    @NotBlank
    @Size(min = 8, max = 30, message = "8~30자 이내로 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$",
            message = "최소 하나의 문자, 하나의 숫자, 하나의 특수문자를 포함해 입력해주세요.")
    private String password;

    @NotBlank
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
            message = "핸드폰번호 형식에 맞게 입력해주세요.(01x-xxxx-xxxx)")
    private String phone;

    @NotBlank
    @Email
    private String email;

}
