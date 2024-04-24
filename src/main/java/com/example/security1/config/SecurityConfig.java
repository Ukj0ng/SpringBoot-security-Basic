package com.example.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // spring-security filter(여기서는 SecurityConfig)가 spring filter chain에 등록됨
@EnableMethodSecurity (securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize, postAuthorize 활성화
public class SecurityConfig{

    // Bean을 붙히면 해당 메서드의 리턴되는 오브젝트를 IoC에 등록해준다.
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .disable())
            .authorizeHttpRequests(auth -> auth     // authorizeHttpRequest는 http요청에 대한 접근을 제어
                .requestMatchers("/user/**").authenticated()    //authenticated() 해당 url에 접근하려면 인증되어야함
                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")  // 해당 권한이 있으면 접근 가능
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()       // 그 외의 모든 요청은 누구나 접근 가능
            )
            .formLogin(form -> form     // formLogin이 활성화되면, 권한이 필요한 페이지에 인증되지 않은 상태로 접근하면
                .loginPage("/loginForm")    // 이 loginPage로 리디렉션함
                .loginProcessingUrl("/login")   // /login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해줌
                .defaultSuccessUrl("/")
            )
            .oauth2Login(oauth -> oauth
                .loginPage("/loginForm")
            );
        return http.build();
    }
}
