package com.movie.Gemflix.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.servlet.http.Cookie;

//사용자에게 반환될 JWT를 담은 Response
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String accessToken;
//    private Cookie accessToken;
    private Cookie refreshToken;

}
