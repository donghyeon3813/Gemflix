package com.movie.Gemflix.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OAuthToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private int refresh_token_expires_in;
}