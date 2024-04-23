package com.example.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // View를 리턴하겠다
public class IndexController {

    // localhost:8080/
    // localhost:8080
    @GetMapping({"", "/"})
    public String index() {
        // thymeleaf 기본폴더 src/main/resources/
        // view resolve 설정: templates(prefix), .html(suffix)
        return "index.html";
    }
}
