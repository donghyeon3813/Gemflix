package com.movie.Gemflix.security.model;

import com.movie.Gemflix.entity.MemberRole;
import lombok.*;

import javax.servlet.http.Cookie;

//사용자에게 반환될 JWT를 담은 Response
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String accessToken;
    private String refreshToken;
    private String memberId;
    private String memberRole;
    private Long expire;

}