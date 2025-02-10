package com.codeit.moim.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {
    @GetMapping("/login/test")
    public String hello() {
        return "/login/test 요청되었습니다.";
    }
}
