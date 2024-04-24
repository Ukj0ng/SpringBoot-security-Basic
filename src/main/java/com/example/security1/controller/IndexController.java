package com.example.security1.controller;

import com.example.security1.model.User;
import com.example.security1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View를 리턴하겠다
public class IndexController {

    @Autowired
    private UserService userService;
    // localhost:8080/
    // localhost:8080
    @GetMapping({"", "/"})
    public String index() {
        // thymeleaf 기본폴더 src/main/resources/
        // view resolve 설정: templates(prefix), .html(suffix)
        return "index";
    }

    @GetMapping("/user")
    public String user() {
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
