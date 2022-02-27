package com.movie.Gemflix.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil{

    public static final long JWT_ACCESS_TOKEN_EXPIRE = 2 * 60 * 60 * 1000; //2시간
    public static final long JWT_REFRESH_TOKEN_EXPIRE = 6 * 60 * 60 * 1000; //6시간

    //for test
//    public static final long JWT_ACCESS_TOKEN_EXPIRE = 30 * 1000; //30초
//    public static final long JWT_REFRESH_TOKEN_EXPIRE = 1 * 60 * 1000; //1분

    public static final String ACCESS_TOKEN_NAME = "accessToken";
    public static final String REFRESH_TOKEN_NAME = "refreshToken";

    @Value("${jwt.secret.key}")
    private String secretKey;

    //create access token
    public String generateToken(String username) {
        return doGenerateToken(username, JWT_ACCESS_TOKEN_EXPIRE);
    }

    //create refresh token
    public String generateRefreshToken(String username) {
        return doGenerateToken(username, JWT_REFRESH_TOKEN_EXPIRE);
    }

    public String doGenerateToken(String username, Long expiredTime){
        return Jwts.builder()
                .setClaims(Jwts.claims()) //claims: token에 담을 정보
                .setSubject(username) //subject: Token (sub에 사용자 이름 넣기)
                .setIssuedAt(new Date(System.currentTimeMillis())) //issuedate: Token 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime)) //expiration: Token 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey).compact(); //알고리즘, 비밀키
    }


    //get username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //get expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}