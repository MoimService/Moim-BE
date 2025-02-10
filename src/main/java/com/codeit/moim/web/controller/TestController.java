package com.codeit.moim.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {
    @GetMapping("/test")
    public String hello() {
        return "/CICD 후 test 페이지입니다. Security config에 permitAll()로 설정되어 있습니다";
    }
}
