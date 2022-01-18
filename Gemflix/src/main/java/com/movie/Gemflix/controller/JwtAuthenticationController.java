package com.movie.Gemflix.controller;

import com.movie.Gemflix.security.util.CookieUtil;
import com.movie.Gemflix.security.util.JwtTokenUtil;
import com.movie.Gemflix.security.model.JwtRequest;
import com.movie.Gemflix.security.model.JwtResponse;
import com.movie.Gemflix.security.service.UserDetailsServiceImpl;
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
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CookieUtil cookieUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest,
                                                       HttpServletResponse response) throws Exception {
        try {
            log.info("jwtRequest: {}", jwtRequest.toString());
            authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
            final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
            String username = userDetails.getUsername();
            final String token = jwtTokenUtil.generateToken(username);
            final String refreshJwtToken = jwtTokenUtil.generateRefreshToken(username);
            Cookie accessToken = cookieUtil.createCookie(
                    JwtTokenUtil.ACCESS_TOKEN_NAME, JwtTokenUtil.JWT_ACCESS_TOKEN_EXPIRE, token);
            Cookie refreshToken = cookieUtil.createCookie(
                    JwtTokenUtil.REFRESH_TOKEN_NAME, JwtTokenUtil.JWT_REFRESH_TOKEN_EXPIRE, refreshJwtToken);
            //TODO: Redis 만료시간 설정
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
