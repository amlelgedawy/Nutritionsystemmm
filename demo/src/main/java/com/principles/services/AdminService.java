package com.principles.services;

import com.principles.models.Admin;
import com.principles.models.User;
import com.principles.models.Coach;
import com.principles.models.FoodItem;
import com.principles.models.Feedback;
import com.principles.repositories.AdminRepository;
import com.principles.repositories.UserRepository;
import com.principles.repositories.CoachRepository;
import com.principles.repositories.FoodItemRepository;
import com.principles.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CoachRepository coachRepository;
    
    @Autowired
    private FoodItemRepository foodItemRepository;
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private NotificationService notificationService;

    public Admin registerAdmin(Admin admin) {
        // Validate admin data
        if (admin.getUsername() == null || admin.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (admin.getEmail() == null || admin.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (adminRepository.findByUsername(admin.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Encode password
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        
        // Save admin
        return adminRepository.save(admin);
    }
    
    public Optional<Admin> findById(String id) {
        return adminRepository.findById(id);
    }
    
    public Optional<Admin> findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }
    
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }
    
    // User Management
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public void deactivateUser(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // In a real implementation, you might have an isActive field
            userRepository.delete(user);
            
            notificationService.sendNotification(
                notificationService.createNotification("email", userId, 
                    "Your account has been deactivated by an administrator.")
            );
        }
    }
    
    // Coach Management
    public List<Coach> getAllCoaches() {
        return coachRepository.findAll();
    }
    
    public Coach approveCoach(String coachId) {
        Optional<Coach> coachOpt = coachRepository.findById(coachId);
        if (coachOpt.isPresent()) {
            Coach coach = coachOpt.get();
            coach.setActive(true);
            Coach savedCoach = coachRepository.save(coach);
            
            notificationService.sendNotification(
                notificationService.createNotification("email", coachId, 
                    "Your coach account has been approved!")
            );
            
            return savedCoach;
        }
        throw new RuntimeException("Coach not found");
    }
    
    public void deactivateCoach(String coachId) {
        Optional<Coach> coachOpt = coachRepository.findById(coachId);
        if (coachOpt.isPresent()) {
            Coach coach = coachOpt.get();
            coach.setActive(false);
            coachRepository.save(coach);
            
            notificationService.sendNotification(
                notificationService.createNotification("email", coachId, 
                    "Your coach account has been deactivated.")
            );
        }
    }
    
    // Food Management
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }
    
    public FoodItem addFoodItem(FoodItem foodItem) {
        foodItem.setVerified(true);
        return foodItemRepository.save(foodItem);
    }
    
    public FoodItem updateFoodItem(FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }
    
    public void deleteFoodItem(String foodId) {
        foodItemRepository.deleteById(foodId);
    }
    
    public FoodItem verifyFoodItem(String foodId) {
        Optional<FoodItem> foodOpt = foodItemRepository.findById(foodId);
        if (foodOpt.isPresent()) {
            FoodItem food = foodOpt.get();
            food.setVerified(true);
            return foodItemRepository.save(food);
        }
        throw new RuntimeException("Food item not found");
    }
    
    // Feedback Management
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }
    
    public List<Feedback> getPendingFeedback() {
        return feedbackRepository.findPendingFeedback();
    }
    
    public Feedback respondToFeedback(String feedbackId, String response) {
        Optional<Feedback> feedbackOpt = feedbackRepository.findById(feedbackId);
        if (feedbackOpt.isPresent()) {
            Feedback feedback = feedbackOpt.get();
            feedback.setAdminResponse(response);
            feedback.setStatus("resolved");
            feedback.setUpdatedAt(new Date());
            
            Feedback savedFeedback = feedbackRepository.save(feedback);
            
            // Notify user about the response
            notificationService.sendNotification(
                notificationService.createNotification("email", feedback.getUserId(), 
                    "Your feedback has been reviewed and responded to.")
            );
            
            return savedFeedback;
        }
        throw new RuntimeException("Feedback not found");
    }
    
    // System Statistics
    public long getTotalUsers() {
        return userRepository.count();
    }
    
    public long getTotalCoaches() {
        return coachRepository.count();
    }
    
    public long getTotalFoodItems() {
        return foodItemRepository.count();
    }
    
    public long getPendingFeedbackCount() {
        return feedbackRepository.findPendingFeedback().size();
    }
}