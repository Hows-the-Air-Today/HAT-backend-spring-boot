package io.howstheairtoday.config;

import java.util.Arrays;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import io.howstheairtoday.filter.MemberLoginFilter;
import io.howstheairtoday.filter.RefreshTokenFilter;
import io.howstheairtoday.filter.TokenCheckFilter;
import io.howstheairtoday.service.MemberDetailsService;
import io.howstheairtoday.service.handler.LoginSuccessHandler;
import io.howstheairtoday.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class CustomSecurityConfig {

    // CORS 설정을 위한 Bean을 생성
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration configuration = new CorsConfiguration();
        // 모든 요청에 설정
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // 메서드 설정
        configuration.setAllowedMethods(Arrays.asList(
            "HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        // 헤더 설정
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization", "Cache-Control", "Content-Type"));
        //인증 설정
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private final MemberDetailsService memberDetailsService;
    private final JwtUtil jwtUtil;

    private TokenCheckFilter tokenCheckFilter(JwtUtil jwtUtil) {
        return new TokenCheckFilter(jwtUtil);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("🛠️ configure -------------------- 🛠️");

        /**
         * AuthenticationManagerBuilder
         * 인증 정보를 제공하는 userDetailsService와 비밀번호 인코딩을 위한 passwordEncoder를 설정하는 빌더 클래스
         */
        AuthenticationManagerBuilder authenticationManagerBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(memberDetailsService).passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http.authenticationManager(authenticationManager);

        /**
         * authenticationManager는 인증 처리를 위한 AuthenticationManager 객체를 의미
         * MemberDetailsService 클래스를 통해 유저 정보를 조회하고, 인증 처리를 수행
         * Spring Security를 사용할 경우 로그인 처리 로직을 직접 작성하지 않고,
         * Spring Security가 제공하는 로그인 기능을 사용
         */
        MemberLoginFilter memberLoginFilter = new MemberLoginFilter("/api/**/auth/login");
        memberLoginFilter.setAuthenticationManager(authenticationManager);

        /**
         * MemberLoginFilter의 setAuthenticationSuccessHandler() 메서드를 호출
         * LoginSuccessHandler 객체를 등록함으로써 로그인 성공 시 LoginSuccessHandler 클래스의 onAuthenticationSuccess() 메서드가 호출되도록 설정
         */
        LoginSuccessHandler loginSuccessHandler = new LoginSuccessHandler(jwtUtil);
        memberLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);

        http.addFilterBefore(memberLoginFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(tokenCheckFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(new RefreshTokenFilter("/api/**/auth/login", jwtUtil), TokenCheckFilter.class);

        http.csrf().disable();
        // Spring Security에서 세션을 사용하지 않도록 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });
        return http.build();
    }

    /**
     * Spring Security에서 정적 자원(static resources)에 대한 요청을 무시하도록 설정
     * [1] 모든 요청에 대해 인가를 수행하면 애플리케이션의 성능에 영향을 줄 수 있다.
     * [2] 정적 자원에 대한 요청은 일반적으로 보안상의 이슈가 없기 때문에 애플리케이션의 성능을 향상 시키기 위해서 설정
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        log.info("🛠️ web configure -------------------- 🛠️");
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
