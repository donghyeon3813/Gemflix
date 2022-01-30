package com.movie.Gemflix.security.filter;

import com.movie.Gemflix.security.util.CookieUtil;
import com.movie.Gemflix.security.util.JwtUtil;
import com.movie.Gemflix.security.service.UserDetailsServiceImpl;
import com.movie.Gemflix.security.util.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
//Client의 Request를 Intercept해서 Header의 Token가 유효한지 검증
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("REQUESTURI : {}", request.getRequestURI());
        String authHeader = request.getHeader("Authorization");
        log.info("authHeader: {}", authHeader);

        //accessToken
        final Cookie accessTokenCookie = cookieUtil.getCookie(request, JwtUtil.ACCESS_TOKEN_NAME);
        log.info("accessTokenCookie: {}", accessTokenCookie);

        String accessToken = null;
        String refreshToken = null;
        String username = null;
        String refreshUsername = null;

        //accessToken 유효성 검사
        try{
            if(accessTokenCookie != null){
                accessToken = accessTokenCookie.getValue();
                username = jwtUtil.getUsernameFromToken(accessToken);
            }
            if(username != null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if(jwtUtil.validateToken(accessToken, userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

        }catch (ExpiredJwtException e) {
            log.info("JWT Token has expired");
            //accessToken 만료시 refreshToken 유효성 검사
            Cookie refreshTokenCookie = cookieUtil.getCookie(request, JwtUtil.REFRESH_TOKEN_NAME);
            if(refreshTokenCookie != null){
                refreshToken  = refreshTokenCookie.getValue();
            }
        }catch(Exception e){
            log.info("Exception Cause: {}", e.getMessage());
        }

        //refreshToken 유효성 검사
        try{
            if(refreshToken != null){
                refreshUsername = redisUtil.getStringData(RedisUtil.PREFIX_REFRESH_TOKEN_KEY + refreshToken);

                if(refreshUsername.equals(jwtUtil.getUsernameFromToken(refreshToken))){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(refreshUsername);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                    //새로운 accessToken 발급
                    String newAccessToken = jwtUtil.generateToken(username);

                    Cookie newAccessTokenCookie = cookieUtil.createCookie(
                            JwtUtil.ACCESS_TOKEN_NAME, JwtUtil.JWT_ACCESS_TOKEN_EXPIRE, newAccessToken);
                    response.addCookie(newAccessTokenCookie);
                }
            }
        }catch(ExpiredJwtException e){
            log.info("Exception Cause: {}", e.getMessage());
        }

        //다음 필터의 단계로 넘어가는 역할
        filterChain.doFilter(request, response);
    }

}
