package com.principles.services;

import com.principles.models.User;
import com.principles.models.Progress;
import com.principles.models.IntakeLog;
import com.principles.repositories.UserRepository;
import com.principles.repositories.ProgressRepository;
import com.principles.repositories.IntakeLogRepository;
import com.principles.utils.CalorieCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Date;
import java.util.Calendar;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProgressRepository progressRepository;
    
    @Autowired
    private IntakeLogRepository intakeLogRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CalorieCalculator calorieCalculator;
    
    @Autowired
    private NotificationService notificationService;

    // Template Method Pattern - defines the skeleton of an algorithm
    public abstract class UserRegistrationTemplate {
        
        // Template method - defines the algorithm structure
        public final User registerUser(User user) {
            validateUserData(user);
            preprocessUserData(user);
            User savedUser = saveUser(user);
            performPostRegistrationActions(savedUser);
            return savedUser;
        }
        
        // These steps can be overridden by subclasses
        protected abstract void validateUserData(User user);
        
        protected void preprocessUserData(User user) {
            // Default implementation - encode password
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        private User saveUser(User user) {
            return userRepository.save(user);
        }
        
        protected void performPostRegistrationActions(User user) {
            // Default implementation - send welcome notification
            notificationService.sendNotification(
                notificationService.createNotification("email", user.getId(), 
                    "Welcome to NutritionX! Your account has been created successfully.")
            );
        }
    }
    
    // Concrete implementation of the template
    private class StandardUserRegistration extends UserRegistrationTemplate {
        @Override
        protected void validateUserData(User user) {
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty");
            }
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                throw new IllegalArgumentException("Username already exists");
            }
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already exists");
            }
        }
    }
    
    // Concrete implementation with additional steps
    private class PremiumUserRegistration extends UserRegistrationTemplate {
        @Override
        protected void validateUserData(User user) {
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty");
            }
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }
            // Additional validation for premium users
            if (user.getHeight() <= 0 || user.getWeight() <= 0) {
                throw new IllegalArgumentException("Height and weight must be provided for premium users");
            }
        }
        
        @Override
        protected void performPostRegistrationActions(User user) {
            // Call the parent implementation
            super.performPostRegistrationActions(user);
            
            // Additional actions for premium users
            notificationService.sendNotification(
                notificationService.createNotification("email", user.getId(), 
                    "Thank you for choosing our premium plan! A coach will be assigned to you shortly.")
            );
        }
    }
    
    private UserRegistrationTemplate standardRegistration = new StandardUserRegistration();
    private UserRegistrationTemplate premiumRegistration = new PremiumUserRegistration();
    
    public User registerStandardUser(User user) {
        user.setRole("USER");
        return standardRegistration.registerUser(user);
    }
    
    public User registerPremiumUser(User user) {
        user.setRole("PREMIUM_USER");
        return premiumRegistration.registerUser(user);
    }
    
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
    
    public List<User> findByCoach(String coachId) {
        return userRepository.findByCoachId(coachId);
    }
    
    public double calculateDailyCalories(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return calorieCalculator.calculateDailyCalories(userOpt.get());
        }
        throw new RuntimeException("User not found");
    }
    
    public Progress recordProgress(String userId, Progress progress) {
        progress.setUserId(userId);
        return progressRepository.save(progress);
    }
    
    public List<Progress> getUserProgress(String userId) {
        return progressRepository.findByUserId(userId);
    }
    
    public IntakeLog logFoodIntake(String userId, IntakeLog intakeLog) {
        intakeLog.setUserId(userId);
        return intakeLogRepository.save(intakeLog);
    }
    
    public List<IntakeLog> getUserIntakeLogs(String userId) {
        return intakeLogRepository.findByUserId(userId);
    }
    
    public List<IntakeLog> getTodayIntakeLogs(String userId) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfDay = cal.getTime();
        
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date startOfNextDay = cal.getTime();
        
        return intakeLogRepository.findByUserIdAndDateRange(userId, startOfDay, startOfNextDay);
    }
}