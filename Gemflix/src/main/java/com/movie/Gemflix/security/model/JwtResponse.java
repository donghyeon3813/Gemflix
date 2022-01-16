package com.movie.Gemflix.security.model;

//사용자에게 반환될 JWT를 담은 Response
public class JwtResponse {

    private final String jwtToken;

    public JwtResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken(){
        return this.jwtToken;
    }

}
