package com.principles.services;

import com.principles.models.Coach;
import com.principles.models.User;
import com.principles.models.DietPlan;
import com.principles.repositories.CoachRepository;
import com.principles.repositories.UserRepository;
import com.principles.repositories.DietPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CoachService {

    @Autowired
    private CoachRepository coachRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DietPlanRepository dietPlanRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private NotificationService notificationService;

    public Coach registerCoach(Coach coach) {
        // Validate coach data
        if (coach.getUsername() == null || coach.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (coach.getEmail() == null || coach.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (coachRepository.findByUsername(coach.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (coachRepository.findByEmail(coach.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Encode password
        coach.setPassword(passwordEncoder.encode(coach.getPassword()));
        
        // Save coach
        Coach savedCoach = coachRepository.save(coach);
        
        // Send notification
        notificationService.sendNotification(
            notificationService.createNotification("email", savedCoach.getId(), 
                "Welcome to NutritionX Coach Panel! Your account has been created successfully.")
        );
        
        return savedCoach;
    }
    
    public Optional<Coach> findById(String id) {
        return coachRepository.findById(id);
    }
    
    public Optional<Coach> findByUsername(String username) {
        return coachRepository.findByUsername(username);
    }
    
    public Optional<Coach> findByEmail(String email) {
        return coachRepository.findByEmail(email);
    }
    
    public List<Coach> findAll() {
        return coachRepository.findAll();
    }
    
    public List<Coach> findActiveCoaches() {
        return coachRepository.findByIsActive(true);
    }
    
    public Coach updateCoach(Coach coach) {
        return coachRepository.save(coach);
    }
    
    public void deleteCoach(String id) {
        coachRepository.deleteById(id);
    }
    
    public List<User> getCoachClients(String coachId) {
        return userRepository.findByCoachId(coachId);
    }
    
    public User assignClientToCoach(String coachId, String userId) {
        Optional<Coach> coachOpt = coachRepository.findById(coachId);
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (coachOpt.isPresent() && userOpt.isPresent()) {
            Coach coach = coachOpt.get();
            User user = userOpt.get();
            
            // Add client to coach's client list
            if (!coach.getClientIds().contains(userId)) {
                coach.getClientIds().add(userId);
                coachRepository.save(coach);
            }
            
            // Assign coach to user
            user.setCoachId(coachId);
            User savedUser = userRepository.save(user);
            
            // Send notification
            notificationService.sendNotification(
                notificationService.createNotification("email", userId, 
                    "You have been assigned a coach: " + coach.getName())
            );
            
            return savedUser;
        }
        
        throw new RuntimeException("Coach or User not found");
    }
    
    public List<DietPlan> getCoachDietPlans(String coachId) {
        return dietPlanRepository.findByCoachId(coachId);
    }
    
    public List<Coach> findAvailableCoaches(int maxClients) {
        return coachRepository.findAvailableCoaches(maxClients);
    }
}