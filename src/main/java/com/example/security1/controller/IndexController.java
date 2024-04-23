package com.example.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View를 리턴하겠다
public class IndexController {

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
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/join")
    public String join() {
        return "join";
    }

    @GetMapping("/joinProc")
    public @ResponseBody String joinProc() {
        return "회원가입 완료됨!";
    }
}
