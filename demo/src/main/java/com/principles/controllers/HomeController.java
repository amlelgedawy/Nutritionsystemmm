package com.principles.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/auth/login";
    }
    
    @GetMapping("/home")
    public String homePage() {
        return "redirect:/auth/login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/user/dashboard";
    }
}