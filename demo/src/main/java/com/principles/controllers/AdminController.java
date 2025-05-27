package com.principles.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.principles.models.Admin;
import com.principles.models.Coach;
import com.principles.models.Feedback;
import com.principles.models.FoodItem;
import com.principles.models.User;
import com.principles.services.AdminService;
import com.principles.services.FoodService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private FoodService foodService;

    @GetMapping("/manage-food")
    public ModelAndView manageFoodPage(@RequestParam String adminId, Model model) {
        Optional<Admin> adminOpt = adminService.findById(adminId);
        if (adminOpt.isPresent()) {
            List<FoodItem> foodItems = adminService.getAllFoodItems();
            model.addAttribute("admin", adminOpt.get());
            model.addAttribute("foodItems", foodItems);
            return new ModelAndView("admin/manage-food");
        }
        return new ModelAndView("redirect:/auth/login");
    }
    
    @GetMapping("/view-feedback")
    public ModelAndView viewFeedbackPage(@RequestParam String adminId, Model model) {
        Optional<Admin> adminOpt = adminService.findById(adminId);
        if (adminOpt.isPresent()) {
            List<Feedback> feedbackList = adminService.getAllFeedback();
            List<Feedback> pendingFeedback = adminService.getPendingFeedback();
            model.addAttribute("admin", adminOpt.get());
            model.addAttribute("feedbackList", feedbackList);
            model.addAttribute("pendingFeedback", pendingFeedback);
            return new ModelAndView("admin/view-feedback");
        }
        return new ModelAndView("redirect:/auth/login");
    }
    
    @GetMapping("/dashboard")
    public ModelAndView dashboard(@RequestParam String adminId, Model model) {
        Optional<Admin> adminOpt = adminService.findById(adminId);
        if (adminOpt.isPresent()) {
            model.addAttribute("admin", adminOpt.get());
            model.addAttribute("totalUsers", adminService.getTotalUsers());
            model.addAttribute("totalCoaches", adminService.getTotalCoaches());
            model.addAttribute("totalFoodItems", adminService.getTotalFoodItems());
            model.addAttribute("pendingFeedbackCount", adminService.getPendingFeedbackCount());
            return new ModelAndView("admin/dashboard");
        }
        return new ModelAndView("redirect:/auth/login");
    }

    // REST API Endpoints
    @GetMapping("/api/admin/users")
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @DeleteMapping("/api/admin/users/{userId}")
    @ResponseBody
    public ResponseEntity<Void> deactivateUser(@PathVariable String userId) {
        try {
            adminService.deactivateUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/api/admin/coaches")
    @ResponseBody
    public ResponseEntity<List<Coach>> getAllCoaches(List<Coach> allCoaches) {
        List<Coach> coaches = allCoaches;
        return ResponseEntity.ok(coaches);
    }
    
    @PutMapping("/api/admin/coaches/{coachId}/approve")
    @ResponseBody
    public ResponseEntity<Coach> approveCoach(@PathVariable String coachId) {
        try {
            Coach approvedCoach = (Coach) adminService.approveCoach(coachId);
            return ResponseEntity.ok(approvedCoach);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/api/admin/coaches/{coachId}/deactivate")
    @ResponseBody
    public ResponseEntity<Void> deactivateCoach(@PathVariable String coachId) {
        try {
            adminService.deactivateCoach(coachId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/api/admin/food-items")
    @ResponseBody
    public ResponseEntity<List<FoodItem>> getAllFoodItems() {
        List<FoodItem> foodItems = adminService.getAllFoodItems();
        return ResponseEntity.ok(foodItems);
    }
    
    @PostMapping("/api/admin/food-items")
    @ResponseBody
    public ResponseEntity<FoodItem> addFoodItem(@RequestBody FoodItem foodItem) {
        FoodItem savedFoodItem = adminService.addFoodItem(foodItem);
        return ResponseEntity.ok(savedFoodItem);
    }
    
    @PutMapping("/api/admin/food-items/{foodId}")
    @ResponseBody
    public ResponseEntity<FoodItem> updateFoodItem(@PathVariable String foodId, @RequestBody FoodItem foodItem) {
        foodItem.setId(foodId);
        FoodItem updatedFoodItem = adminService.updateFoodItem(foodItem);
        return ResponseEntity.ok(updatedFoodItem);
    }
    
    @DeleteMapping("/api/admin/food-items/{foodId}")
    @ResponseBody
    public ResponseEntity<Void> deleteFoodItem(@PathVariable String foodId) {
        try {
            adminService.deleteFoodItem(foodId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/api/admin/food-items/{foodId}/verify")
    @ResponseBody
    public ResponseEntity<FoodItem> verifyFoodItem(@PathVariable String foodId) {
        try {
            FoodItem verifiedFood = adminService.verifyFoodItem(foodId);
            return ResponseEntity.ok(verifiedFood);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/api/admin/feedback")
    @ResponseBody
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        List<Feedback> feedback = adminService.getAllFeedback();
        return ResponseEntity.ok(feedback);
    }
    
    @GetMapping("/api/admin/feedback/pending")
    @ResponseBody
    public ResponseEntity<List<Feedback>> getPendingFeedback() {
        List<Feedback> feedback = adminService.getPendingFeedback();
        return ResponseEntity.ok(feedback);
    }
    
    @PutMapping("/api/admin/feedback/{feedbackId}/respond")
    @ResponseBody
    public ResponseEntity<Feedback> respondToFeedback(@PathVariable String feedbackId, @RequestBody FeedbackResponse response) {
        try {
            Feedback updatedFeedback = adminService.respondToFeedback(feedbackId, response.getResponse());
            return ResponseEntity.ok(updatedFeedback);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/api/admin/statistics")
    @ResponseBody
    public ResponseEntity<SystemStatistics> getSystemStatistics() {
        SystemStatistics stats = new SystemStatistics();
        stats.setTotalUsers(adminService.getTotalUsers());
        stats.setTotalCoaches(adminService.getTotalCoaches());
        stats.setTotalFoodItems(adminService.getTotalFoodItems());
        stats.setPendingFeedbackCount(adminService.getPendingFeedbackCount());
        return ResponseEntity.ok(stats);
    }
    
    // DTOs
    public static class FeedbackResponse {
        private String response;
        
        public String getResponse() { return response; }
        public void setResponse(String response) { this.response = response; }
    }
    
    public static class SystemStatistics {
        private long totalUsers;
        private long totalCoaches;
        private long totalFoodItems;
        private long pendingFeedbackCount;
        
        // Getters and setters
        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
        
        public long getTotalCoaches() { return totalCoaches; }
        public void setTotalCoaches(long totalCoaches) { this.totalCoaches = totalCoaches; }
        
        public long getTotalFoodItems() { return totalFoodItems; }
        public void setTotalFoodItems(long totalFoodItems) { this.totalFoodItems = totalFoodItems; }
        
        public long getPendingFeedbackCount() { return pendingFeedbackCount; }
        public void setPendingFeedbackCount(long pendingFeedbackCount) { this.pendingFeedbackCount = pendingFeedbackCount; }
    }
}