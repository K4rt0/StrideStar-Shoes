package com.stores.stridestar.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping({ "", "/", "/home" })
    public String home() {
        return "/main-site/home/hello";
    }

    @GetMapping("/login")
    public String login() {
        return "/main-site/user/login";
    }
}
