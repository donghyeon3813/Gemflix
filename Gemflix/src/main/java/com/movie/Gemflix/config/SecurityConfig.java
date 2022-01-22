package com.movie.Gemflix.config;

import com.movie.Gemflix.security.filter.JwtRequestFilter;
import com.movie.Gemflix.security.service.UserDetailsServiceImpl;
import com.movie.Gemflix.security.util.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity //SpringSecurityFilterChain이 자동으로 포함
@EnableGlobalMethodSecurity(prePostEnabled = true) //@PreAuthorize, @PostAuthorize 을 사용하여 인가 처리를 하고 싶을때 사용하는 옵션
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder delegatingPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override //AuthenticationManager 설정
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //UserDetailsService(를 상속한 클래스)를 Bean으로 등록하면
        //Security가 자동으로 해당 클래스를 UserDetailsService로 생각하지만 나는 명시적으로 설정함
        auth.userDetailsService(userDetailsService).passwordEncoder(delegatingPasswordEncoder());
    }

    //role 계층 설정
    private SecurityExpressionHandler<FilterInvocation> expressionHandler() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MEMBER > ROLE_NONE");

        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy);
        return defaultWebSecurityExpressionHandler;
    }

    @Override //허가 필요한 리소스 설정
    protected void configure(HttpSecurity http) throws Exception {

        http
                //cors 허용 적용
                .cors().configurationSource(corsConfigurationSource())

                //csrf 설정 Disable
                .and()
                .csrf().disable()

                //exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)

                //시큐리티는 기본적으로 세션을 사용, But 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // authenticate 외 나머지는 모두 인증 필요
                .and()
                .authorizeRequests()
                .expressionHandler(expressionHandler())
                .antMatchers("/authenticate").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/member/**").hasRole("MEMBER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()

                //jwtRequestFilter 를 addFilterBefore 로 등록 (UsernamePasswordAuthenticationFilter 필터 이전에 실행)
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                ;

        http.formLogin(); //문제시 로그인 화면으로
        http.logout();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.addAllowedOriginPattern("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
