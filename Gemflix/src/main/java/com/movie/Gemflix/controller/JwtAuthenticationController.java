package com.movie.Gemflix.controller;

import com.movie.Gemflix.security.util.CookieUtil;
import com.movie.Gemflix.security.util.JwtUtil;
import com.movie.Gemflix.security.model.JwtRequest;
import com.movie.Gemflix.security.model.JwtResponse;
import com.movie.Gemflix.security.service.UserDetailsServiceImpl;
import com.movie.Gemflix.security.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;
    private final UserDetailsServiceImpl userDetailsService;


    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest,
                                                       HttpServletResponse response) throws Exception {
        try {
            log.info("jwtRequest: {}", jwtRequest.toString());
            authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
            final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
            String username = userDetails.getUsername();
            final String token = jwtUtil.generateToken(username);
            final String refreshJwtToken = jwtUtil.generateRefreshToken(username);
            Cookie accessToken = cookieUtil.createCookie(
                    jwtUtil.ACCESS_TOKEN_NAME, jwtUtil.JWT_ACCESS_TOKEN_EXPIRE, token);
            Cookie refreshToken = cookieUtil.createCookie(
                    jwtUtil.REFRESH_TOKEN_NAME, jwtUtil.JWT_REFRESH_TOKEN_EXPIRE, refreshJwtToken);

            //Redis에 Refresh Token 저장 후 만료시간 설정
            redisUtil.setDataExpire(refreshJwtToken, username, jwtUtil.JWT_REFRESH_TOKEN_EXPIRE);

            response.addCookie(accessToken);
            response.addCookie(refreshToken);
            return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("fail to login.");
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}