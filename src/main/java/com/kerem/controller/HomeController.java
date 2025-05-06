package com.kerem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {

    @GetMapping
    public String home() {
        return "Welcome to the home page!";
    }

    @GetMapping("/api")
    public String secureHome() {
        return "Welcome to the secure home page!";
    }
}
