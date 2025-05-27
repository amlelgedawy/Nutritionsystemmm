package com.principles.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.principles.models.DietPlan;
import com.principles.models.IntakeLog;
import com.principles.models.Progress;
import com.principles.models.User;
import com.principles.services.DietPlanService;
import com.principles.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private DietPlanService dietPlanService;

    @GetMapping("/dashboard")
    public ModelAndView dashboard(@RequestParam String userId, Model model) {
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<IntakeLog> todayLogs = userService.getTodayIntakeLogs(userId);
            List<Progress> progressHistory = userService.getUserProgress(userId);
            
            model.addAttribute("user", user);
            model.addAttribute("todayLogs", todayLogs);
            model.addAttribute("progressHistory", progressHistory);
            
            return new ModelAndView("user/dashboard");
        }
        return new ModelAndView("redirect:/auth/login");
    }
    
    @GetMapping("/log-food")
    public ModelAndView logFoodPage(@RequestParam String userId, Model model) {
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            return new ModelAndView("user/log-food");
        }
        return new ModelAndView("redirect:/auth/login");
    }
    
    @GetMapping("/progress")
    public ModelAndView progressPage(@RequestParam String userId, Model model) {
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isPresent()) {
            List<Progress> progressHistory = userService.getUserProgress(userId);
            model.addAttribute("user", userOpt.get());
            model.addAttribute("progressHistory", progressHistory);
            return new ModelAndView("user/progress");
        }
        return new ModelAndView("redirect:/auth/login");
    }

    // REST API Endpoints
    @PostMapping("/api/users/register/standard")
    @ResponseBody
    public ResponseEntity<User> registerStandardUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerStandardUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/api/users/register/premium")
    @ResponseBody
    public ResponseEntity<User> registerPremiumUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerPremiumUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/api/users/{userId}")
    @ResponseBody
    public ResponseEntity<User> getUserProfile(@PathVariable String userId) {
        Optional<User> userOpt = userService.findById(userId);
        return userOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/api/users/{userId}")
    @ResponseBody
    public ResponseEntity<User> updateUserProfile(@PathVariable String userId, @RequestBody User user) {
        user.setId(userId);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }
    
    @GetMapping("/api/users/{userId}/diet-plan")
    @ResponseBody
    public ResponseEntity<DietPlan> getUserDietPlan(@PathVariable String userId) {
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isPresent() && userOpt.get().getCurrentDietPlan() != null) {
            return ResponseEntity.ok(userOpt.get().getCurrentDietPlan());
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/api/users/{userId}/intake-log")
    @ResponseBody
    public ResponseEntity<IntakeLog> logFoodIntake(@PathVariable String userId, @RequestBody IntakeLog intakeLog) {
        IntakeLog savedLog = userService.logFoodIntake(userId, intakeLog);
        return ResponseEntity.ok(savedLog);
    }
    
    @GetMapping("/api/users/{userId}/intake-logs")
    @ResponseBody
    public ResponseEntity<List<IntakeLog>> getUserIntakeLogs(@PathVariable String userId) {
        List<IntakeLog> logs = userService.getUserIntakeLogs(userId);
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping("/api/users/{userId}/intake-logs/today")
    @ResponseBody
    public ResponseEntity<List<IntakeLog>> getTodayIntakeLogs(@PathVariable String userId) {
        List<IntakeLog> logs = userService.getTodayIntakeLogs(userId);
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping("/api/users/{userId}/progress")
    @ResponseBody
    public ResponseEntity<List<Progress>> getUserProgress(@PathVariable String userId) {
        List<Progress> progress = userService.getUserProgress(userId);
        return ResponseEntity.ok(progress);
    }
    
    @PostMapping("/api/users/{userId}/progress")
    @ResponseBody
    public ResponseEntity<Progress> recordProgress(@PathVariable String userId, @RequestBody Progress progress) {
        Progress savedProgress = userService.recordProgress(userId, progress);
        return ResponseEntity.ok(savedProgress);
    }
    
    @GetMapping("/api/users/{userId}/calories/daily")
    @ResponseBody
    public ResponseEntity<Double> getDailyCalories(@PathVariable String userId) {
        try {
            double dailyCalories = userService.calculateDailyCalories(userId);
            return ResponseEntity.ok(dailyCalories);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}