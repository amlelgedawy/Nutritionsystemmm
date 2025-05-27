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

import com.principles.models.Coach;
import com.principles.models.DietPlan;
import com.principles.models.User;
import com.principles.services.CoachService;
import com.principles.services.DietPlanService;

@Controller
@RequestMapping("/coach")
public class CoachController {

    @Autowired
    private CoachService coachService;
    
    @Autowired
    private DietPlanService dietPlanService;

    @GetMapping("/create-plan")
    public ModelAndView createPlanPage(@RequestParam String coachId, Model model) {
        var coachOpt = coachService.findById(coachId);
        if (coachOpt.isPresent()) {
            List<User> clients = coachService.getCoachClients(coachId);
            model.addAttribute("coach", coachOpt.get());
            model.addAttribute("clients", clients);
            return new ModelAndView("coach/create-plan");
        }
        return new ModelAndView("redirect:/auth/login");
    }
    
    @GetMapping("/view-users")
    public ModelAndView viewUsersPage(@RequestParam String coachId, Model model) {
        Optional<Coach> coachOpt = coachService.findById(coachId);
        if (coachOpt.isPresent()) {
            List<User> clients = coachService.getCoachClients(coachId);
            model.addAttribute("coach", coachOpt.get());
            model.addAttribute("clients", clients);
            return new ModelAndView("coach/view-users");
        }
        return new ModelAndView("redirect:/auth/login");
    }
    
    @GetMapping("/dashboard")
    public ModelAndView dashboard(@RequestParam String coachId, Model model) {
        Optional<Coach> coachOpt = coachService.findById(coachId);
        if (coachOpt.isPresent()) {
            List<User> clients = coachService.getCoachClients(coachId);
            List<DietPlan> dietPlans = coachService.getCoachDietPlans(coachId);
            model.addAttribute("coach", coachOpt.get());
            model.addAttribute("clients", clients);
            model.addAttribute("dietPlans", dietPlans);
            return new ModelAndView("coach/dashboard");
        }
        return new ModelAndView("redirect:/auth/login");
    }

    // REST API Endpoints
    @GetMapping("/api/coaches")
    @ResponseBody
    public ResponseEntity<List<Coach>> getAllCoaches() {
        List<Coach> coaches = coachService.findAll();
        return ResponseEntity.ok(coaches);
    }
    
    @GetMapping("/api/coaches/active")
    @ResponseBody
    public ResponseEntity<List<Coach>> getActiveCoaches() {
        List<Coach> coaches = coachService.findActiveCoaches();
        return ResponseEntity.ok(coaches);
    }
    
    @GetMapping("/api/coaches/{coachId}")
    @ResponseBody
    public ResponseEntity<Coach> getCoach(@PathVariable String coachId) {
        Optional<Coach> coachOpt = coachService.findById(coachId);
        return coachOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/api/coaches/{coachId}")
    @ResponseBody
    public ResponseEntity<Coach> updateCoach(@PathVariable String coachId, @RequestBody Coach coach) {
        coach.setId(coachId);
        Coach updatedCoach = coachService.updateCoach(coach);
        return ResponseEntity.ok(updatedCoach);
    }
    
    @GetMapping("/api/coaches/{coachId}/clients")
    @ResponseBody
    public ResponseEntity<List<User>> getCoachClients(@PathVariable String coachId) {
        List<User> clients = coachService.getCoachClients(coachId);
        return ResponseEntity.ok(clients);
    }
    
    @PostMapping("/api/coaches/{coachId}/assign-client/{userId}")
    @ResponseBody
    public ResponseEntity<User> assignClient(@PathVariable String coachId, @PathVariable String userId) {
        try {
            User assignedUser = coachService.assignClientToCoach(coachId, userId);
            return ResponseEntity.ok(assignedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/api/coaches/{coachId}/diet-plans")
    @ResponseBody
    public ResponseEntity<List<DietPlan>> getCoachDietPlans(@PathVariable String coachId) {
        List<DietPlan> dietPlans = coachService.getCoachDietPlans(coachId);
        return ResponseEntity.ok(dietPlans);
    }
    
    @PostMapping("/api/coaches/{coachId}/diet-plans")
    @ResponseBody
    public ResponseEntity<DietPlan> createDietPlan(@PathVariable String coachId, @RequestBody CreateDietPlanRequest request) {
        try {
            DietPlan dietPlan = dietPlanService.createDietPlan(
                request.getUserId(), 
                coachId, 
                request.getPlanName(), 
                request.getDietaryRestrictions(), 
                request.getGoal()
            );
            return ResponseEntity.ok(dietPlan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // DTO for diet plan creation
    public static class CreateDietPlanRequest {
        private String userId;
        private String planName;
        private List<String> dietaryRestrictions;
        private String goal;
        
        // Getters and setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        
        public String getPlanName() { return planName; }
        public void setPlanName(String planName) { this.planName = planName; }
        
        public List<String> getDietaryRestrictions() { return dietaryRestrictions; }
        public void setDietaryRestrictions(List<String> dietaryRestrictions) { this.dietaryRestrictions = dietaryRestrictions; }
        
        public String getGoal() { return goal; }
        public void setGoal(String goal) { this.goal = goal; }
    }
}