package com.movie.Gemflix.security.filter;

import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.security.util.JwtUtil;
import com.movie.Gemflix.security.service.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
@Component
//Client의 Request를 Intercept해서 Header의 Token가 유효한지 검증
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("REQUESTURI : {}", request.getRequestURI());
        String authHeader = request.getHeader("Authorization");
        log.info("authHeader: {}", authHeader);
        if(authHeader != null){
            String accessToken = null;
            String username = null;

            //accessToken 유효성 검사
            try{
                if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer")){ //jwt 일 경우 : Bearer
                    accessToken = authHeader.substring(7);
                    log.info("accessToken: {}", accessToken);
                    username = jwtUtil.getUsernameFromToken(accessToken);

                    if(username != null){
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        if(jwtUtil.validateToken(accessToken, userDetails)){
                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                    new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        }
                    }
                }

            }catch (ExpiredJwtException e) {
                log.info("access Token has expired");
                //accessToken 만료시 401
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=utf-8");
                JSONObject json02 = new JSONObject();
                json02.put("errorCode", ErrorType.ACCESS_TOKEN_EXPIRED.getErrorCode());
                json02.put("errorMessage", ErrorType.ACCESS_TOKEN_EXPIRED.getErrorMessage());
                JSONObject json = new JSONObject();
                json.put("status", HttpServletResponse.SC_UNAUTHORIZED);
                json.put("message", json02);
                PrintWriter out = response.getWriter();
                out.print(json);
                return;
            }catch(Exception e){
                log.info("Exception Cause: {}", e.getMessage());
            }
        }

        //다음 필터의 단계로 넘어가는 역할
        filterChain.doFilter(request, response);
    }

}