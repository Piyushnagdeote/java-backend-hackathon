package com.company.platform.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping("/open")
    public String open() {
        return "OPEN OK";
    }

    @GetMapping("/secure")
    public String secure() {
        return "JWT is working!";
    }
}