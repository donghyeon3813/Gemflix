package com.movie.Gemflix.security.filter;

import com.movie.Gemflix.security.util.CookieUtil;
import com.movie.Gemflix.security.util.JwtUtil;
import com.movie.Gemflix.security.service.UserDetailsServiceImpl;
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


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("REQUESTURI : " + request.getRequestURI());

        //JWT Token
        final Cookie jwtToken = cookieUtil.getCookie(request, JwtUtil.ACCESS_TOKEN_NAME);

        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;
        String refreshJwt = null;
        String refreshUname = null;

        //JWT Token 유효성 검사
        try{
            if(jwtToken != null){
                jwt = jwtToken.getValue();
                username = jwtUtil.getUsernameFromToken(jwt);
            }
            if(username != null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if(jwtUtil.validateToken(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

        }catch (ExpiredJwtException e) {
            log.info("JWT Token has expired");
            Cookie refreshToken = cookieUtil.getCookie(request, jwtUtil.generateRefreshToken(username));
            if(refreshToken != null){
                refreshJwt  = refreshToken.getValue();
            }
        }catch(Exception e){
            log.info("Exception Cause: {}", e.getMessage());
        }

        //TODO: refreshJwt 유효성 검사

        //다음 필터의 단계로 넘어가는 역할
        filterChain.doFilter(request, response);
    }

}
