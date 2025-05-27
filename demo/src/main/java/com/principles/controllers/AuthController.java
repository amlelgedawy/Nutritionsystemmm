package com.principles.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.principles.models.Admin;
import com.principles.models.Coach;
import com.principles.models.User;
import com.principles.services.AdminService;
import com.principles.services.CoachService;
import com.principles.services.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private CoachService coachService;
    
    @Autowired
    private AdminService adminService;

    @GetMapping("/auth/login")
    public ModelAndView loginPage() {
        ModelAndView mv = new ModelAndView("shared/login");
        return mv;
    }
    
    @GetMapping("/login")
    public String loginRedirect() {
        return "redirect:/auth/login";
    }
    
    @GetMapping("/auth/register")
    public ModelAndView registerPage() {
        ModelAndView mv = new ModelAndView("shared/register");
        return mv;
    }
    
    @GetMapping("/register")
    public String registerRedirect() {
        return "redirect:/auth/register";
    }
    
    @PostMapping("/auth/register/user")
    @ResponseBody
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerStandardUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/auth/register/premium-user")
    @ResponseBody
    public ResponseEntity<User> registerPremiumUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerPremiumUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/auth/register/coach")
    @ResponseBody
    public ResponseEntity<Coach> registerCoach(@RequestBody Coach coach) {
        try {
            Coach registeredCoach = coachService.registerCoach(coach);
            return ResponseEntity.ok(registeredCoach);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/auth/register/admin")
    @ResponseBody
    public ResponseEntity<Admin> registerAdmin(@RequestBody Admin admin) {
        try {
            Admin registeredAdmin = adminService.registerAdmin(admin);
            return ResponseEntity.ok(registeredAdmin);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}