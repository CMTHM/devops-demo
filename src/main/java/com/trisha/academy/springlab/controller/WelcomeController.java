package com.trisha.academy.springlab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/name")
    public String welcome() {
        return "My name is Bhagyashree Behera";
    }
}
