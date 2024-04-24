package com.example.security1.config;

import com.example.security1.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;


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
            )
            // 1. 코드받기(인증), 2. 엑세스토큰(권한),
            // 3. 사용자프로필 정보를 가져오기, 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
            // 4-2. (email, phone number, name, id) -> 받은 정보에 없는 추가적인 정보가 필요함
            .oauth2Login(oauth -> oauth
                .loginPage("/loginForm") // 구글 로그인 왼료된 뒤에 후처리가 필요함. Tip. 코드x, (엑세스토큰 + 사용자프로필 정보O)
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(principalOauth2UserService))
            );
        return http.build();
    }
}