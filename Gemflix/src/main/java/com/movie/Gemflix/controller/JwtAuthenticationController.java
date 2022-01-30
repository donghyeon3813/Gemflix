package com.movie.Gemflix.controller;

import com.movie.Gemflix.common.ApiResponseMessage;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.entity.MemberRole;
import com.movie.Gemflix.security.util.CookieUtil;
import com.movie.Gemflix.security.util.JwtUtil;
import com.movie.Gemflix.security.model.JwtRequest;
import com.movie.Gemflix.security.model.JwtResponse;
import com.movie.Gemflix.security.service.UserDetailsServiceImpl;
import com.movie.Gemflix.security.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
    private final PasswordEncoder passwordEncoder;


    //refreshToken 으로 accessToken 재발급
    @PostMapping("/authenticate")
    public ResponseEntity<?> checkAuthenticationToken(HttpServletRequest request,
                                                      HttpServletResponse response){
        log.info(("=== checkAuthenticationToken"));
        return null;
        /*String token = null;
        String refreshJwtToken = null;
        Cookie accessToken = null;
        Cookie refreshToken = null;

        Cookie [] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0 ) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals(JwtUtil.REFRESH_TOKEN_NAME)){
                    
                    refreshJwtToken = cookie.getValue();
                    if(refreshJwtToken == null || refreshJwtToken.isEmpty()) break;
                        
                    String username = jwtUtil.getUsernameFromToken(refreshJwtToken);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    //memberId, 토큰 유효시간 확인
                    if(jwtUtil.validateToken(refreshJwtToken, userDetails)) {
                        token = jwtUtil.generateToken(username); //access토큰 발급
                        accessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, JwtUtil.JWT_ACCESS_TOKEN_EXPIRE, token);
                        refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, JwtUtil.JWT_REFRESH_TOKEN_EXPIRE, refreshJwtToken);
                        log.info("accessToken: {}, refreshToken: {}", accessToken, refreshToken);

                        response.addCookie(accessToken);
                        response.addCookie(refreshToken);
                        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
                        
                    }
                }
            }
        }
        ApiResponseMessage apiRm = new ApiResponseMessage(HttpStatus.UNAUTHORIZED.value(), ErrorType.INVALID_MEMBER);
        return new ResponseEntity<>(apiRm, HttpStatus.valueOf(apiRm.getStatus()));*/
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