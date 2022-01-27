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


    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest,
                                                       HttpServletResponse response) throws Exception {
        try {
            log.info("jwtRequest: {}", jwtRequest.toString());
            if(!checkMemberAuth(jwtRequest)) {
                ApiResponseMessage apiRm = new ApiResponseMessage(HttpStatus.UNAUTHORIZED.value(), ErrorType.INVALID_MEMBER);
                return new ResponseEntity<>(apiRm, HttpStatus.valueOf(apiRm.getStatus()));
            }

            String username = jwtRequest.getUsername();
            final String token = jwtUtil.generateToken(username);
            final String refreshJwtToken = jwtUtil.generateRefreshToken(username);
            Cookie accessToken = cookieUtil.createCookie(
                    JwtUtil.ACCESS_TOKEN_NAME, JwtUtil.JWT_ACCESS_TOKEN_EXPIRE, token);
            Cookie refreshToken = cookieUtil.createCookie(
                    JwtUtil.REFRESH_TOKEN_NAME, JwtUtil.JWT_REFRESH_TOKEN_EXPIRE, refreshJwtToken);
            log.info("accessToken: {}, refreshToken: {}", accessToken, refreshToken);

            //Redis에 Refresh Token 저장 후 만료시간 설정
            redisUtil.setStringDataExpire(refreshJwtToken, username, JwtUtil.JWT_REFRESH_TOKEN_EXPIRE);

            response.addCookie(accessToken);
            response.addCookie(refreshToken);
            return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));

        }catch (Exception e){
            ApiResponseMessage apiRm = new ApiResponseMessage(HttpStatus.BAD_REQUEST.value(), ErrorType.INVALID_MEMBER);
            return new ResponseEntity<>(apiRm, HttpStatus.valueOf(apiRm.getStatus()));
        }
    }

    private boolean checkMemberAuth(JwtRequest jwtRequest) {
        String id = jwtRequest.getUsername();
        String password = jwtRequest.getPassword();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(id);
        String realId = userDetails.getUsername();
        String realPassword = userDetails.getPassword();
        boolean isValidMember = id.equals(realId) && passwordEncoder.matches(password, realPassword);

        if(!isValidMember) return false; //유효하지않은 회원
        String authority = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toString();
        return !("ROLE_" + MemberRole.NONE).equals(authority); //비회원은 로그인 불가
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