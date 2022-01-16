package com.movie.Gemflix.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//사용자에게서 받은 id, pw를 저장
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtRequest {

    private String username;
    private String password;

}
