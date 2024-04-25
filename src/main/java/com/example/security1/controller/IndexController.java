package com.example.security1.controller;

import com.example.security1.auth.PrincipalDetail;
import com.example.security1.model.User;
import com.example.security1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View를 리턴하겠다
public class IndexController {

    @Autowired
    private UserService userService;

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin (
        Authentication authentication, // DI(의존성 주입)
        @AuthenticationPrincipal OAuth2User oAuth2) {
        System.out.println("/test/oauth/login ==========");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication: " + oAuth2User.getAttributes());
        System.out.println("oAuth2: " + oAuth2.getAttributes());
        return "OAuth 세션 정보 확인하기";
    }

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(
        Authentication authentication, // DI(의존성 주입)
        @AuthenticationPrincipal PrincipalDetail userDetails) {  // @AuthenticationPrincipal를 통해 session에 접근 가능
        System.out.println("/test/login ==========");
        PrincipalDetail principalDetail = (PrincipalDetail) authentication.getPrincipal();
        System.out.println("authentication: " + principalDetail.getUser());
        System.out.println("userDetails: " + userDetails.getUser());
        return "세션 정보 확인하기";
    }
    // localhost:8080/
    // localhost:8080
    @GetMapping({"", "/"})
    public String index() {
        // thymeleaf 기본폴더 src/main/resources/
        // view resolve 설정: templates(prefix), .html(suffix)
        return "index";
    }

    // OAuth 로그인을 해도 PrincipalDetails
    // 일반 로그인을 해도 PrincipalDetails
    // @AuthenticationPrincipal 어노테이션은 언제 활성화될까?
    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        System.out.println("principalDetails: " + principalDetail.getUser());
        return "user";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    // spring-security가 login을 가로챔
    // but SecurityConfig파일을 만들고 나니 작동을 안함
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println(user);
        userService.회원가입(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // 이런건 보통 메서드 하나에만 적용하고 싶을 때 쓰고, 많으면 filterchain가서 쓰면됨.
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')") // data 함수가 실행되기 직전에 실행
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}
