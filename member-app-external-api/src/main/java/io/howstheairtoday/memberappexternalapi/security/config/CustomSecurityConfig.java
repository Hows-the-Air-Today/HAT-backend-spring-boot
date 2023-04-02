package io.howstheairtoday.memberappexternalapi.security.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.howstheairtoday.memberappexternalapi.security.service.MemberDetailsService;
import io.howstheairtoday.memberappexternalapi.security.service.handler.MemberLoginSuccessHandler;
import io.howstheairtoday.memberappexternalapi.security.util.JWTUtil;
import io.howstheairtoday.memberappexternalapi.security.filter.MemberLoginFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Security 설정 클래스
 */
@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class CustomSecurityConfig {

    private final MemberDetailsService memberDetailsService;
    private final JWTUtil jwtUtil;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        log.info("🛠️ configure -------------------- 🛠️");

        // filter.MemberLoginFilter - AuthenticationManager 설정
        AuthenticationManagerBuilder authenticationManagerBuilder =
            httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
            .userDetailsService(memberDetailsService)
            .passwordEncoder(passwordEncoder());

        // Get AuthenticationManager
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        httpSecurity.authenticationManager(authenticationManager);

        // MemberLoginFilter
        // Spring Security에서 username과 password를 처리하는 UsernamePasswordAuthenticationFilter의 앞쪽에서 동작하도록 설정
        MemberLoginFilter memberLoginFilter = new MemberLoginFilter("/api/**/auth/login");
        memberLoginFilter.setAuthenticationManager(authenticationManager);

        // MemberLoginSuccessHandler - 로그인 인증 성공 이후 작업 처리 설정
        MemberLoginSuccessHandler successHandler = new MemberLoginSuccessHandler(jwtUtil);
        memberLoginFilter.setAuthenticationSuccessHandler(successHandler);

        // MemberLoginFilter 위치 조정
        httpSecurity.addFilterBefore(memberLoginFilter, UsernamePasswordAuthenticationFilter.class);

        /*
        // post 기능에 인증된 회원만 접속 가능토록 설정
        httpSecurity.authorizeRequests().requestMatchers("/api/v1/post/**").authenticated().anyRequest().permitAll();
         */

        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        log.info("🛠️ web configure -------------------- 🛠️");
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
