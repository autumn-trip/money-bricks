package com.moneybricks.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .cors(Customizer.withDefaults()) // CORS 설정 (필요 시 추가 설정 가능)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll() // 특정 경로 인증 비활성화
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                );

//                .formLogin(Customizer.withDefaults()); // 기본 로그인 폼 사용
        return http.build();
    }
}
