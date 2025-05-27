package com.principles.services;

import com.principles.models.DietPlan;
import com.principles.models.FoodItem;
import com.principles.models.User;
import com.principles.repositories.DietPlanRepository;
import com.principles.repositories.FoodItemRepository;
import com.principles.repositories.UserRepository;
import com.principles.utils.CalorieCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Facade Pattern - Simplifies the complex subsystem of diet planning
@Service
public class DietPlanService {

    @Autowired
    private DietPlanRepository dietPlanRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FoodItemRepository foodItemRepository;
    
    @Autowired
    private CalorieCalculator calorieCalculator;
    
    @Autowired
    private NotificationService notificationService;
    
    // Facade method that hides the complexity of creating a diet plan
    public DietPlan createDietPlan(String userId, String coachId, String planName, 
                                  List<String> dietaryRestrictions, String goal) {
        
        User user = userRepository.findById(userId).orElseThrow(() -> 
            new RuntimeException("User not found"));
        
        // Calculate calorie needs based on goal
        double dailyCalories;
        switch (goal.toLowerCase()) {
            case "weight_loss":
                dailyCalories = calorieCalculator.calculateWeightLossCalories(user, 0.5); // 0.5 kg per week
                break;
            case "weight_gain":
                dailyCalories = calorieCalculator.calculateWeightGainCalories(user, 0.5); // 0.5 kg per week
                break;
            default: // maintenance
                dailyCalories = calorieCalculator.calculateDailyCalories(user);
        }
        
        // Calculate macronutrient targets
        int proteinTarget = (int) (user.getWeight() * 2); // 2g per kg of body weight
        int fatTarget = (int) ((dailyCalories * 0.25) / 9); // 25% of calories from fat, 9 calories per gram
        int carbsTarget = (int) ((dailyCalories - (proteinTarget * 4) - (fatTarget * 9)) / 4); // Remaining calories from carbs, 4 calories per gram
        
        // Find suitable foods based on dietary restrictions
        List<FoodItem> recommendedFoods = foodItemRepository.findAll().stream()
            .filter(food -> {
                if (dietaryRestrictions == null) return true;
                for (String restriction : dietaryRestrictions) {
                    if (food.getCategories().contains(restriction)) {
                        return false;
                    }
                }
                return true;
            })
            .limit(20) // Limit to 20 recommended foods
            .collect(Collectors.toList());
        
        // Use Builder pattern to create the diet plan
        DietPlan plan = new DietPlan.Builder()
            .withName(planName)
            .forUser(userId)
            .byCoach(coachId)
            .withDuration(new Date(), new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)) // 30 days
            .withCalorieTarget((int) dailyCalories)
            .withMacros(proteinTarget, carbsTarget, fatTarget)
            .withRecommendedFoods(recommendedFoods)
            .withRestrictions(dietaryRestrictions)
            .build();
        
        // Save the plan
        DietPlan savedPlan = dietPlanRepository.save(plan);
        
        // Update user's current diet plan
        user.setCurrentDietPlan(savedPlan);
        userRepository.save(user);
        
        // Notify the user
        notificationService.sendNotification(
            notificationService.createNotification("email", userId, 
                "Your new diet plan '" + planName + "' has been created!")
        );
        
        return savedPlan;
    }
    
    public Optional<DietPlan> findById(String id) {
        return dietPlanRepository.findById(id);
    }
    
    public List<DietPlan> findByUserId(String userId) {
        return dietPlanRepository.findByUserId(userId);
    }
    
    public List<DietPlan> findByCoachId(String coachId) {
        return dietPlanRepository.findByCoachId(coachId);
    }
    
    public List<DietPlan> findActivePlans() {
        return dietPlanRepository.findActivePlans(new Date());
    }
    
    public DietPlan updateDietPlan(DietPlan dietPlan) {
        return dietPlanRepository.save(dietPlan);
    }
    
    public void deleteDietPlan(String id) {
        dietPlanRepository.deleteById(id);
    }
    
    public DietPlan assignDietPlanToUser(String planId, String userId) {
        Optional<DietPlan> planOpt = dietPlanRepository.findById(planId);
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (planOpt.isPresent() && userOpt.isPresent()) {
            DietPlan plan = planOpt.get();
            User user = userOpt.get();
            
            user.setCurrentDietPlan(plan);
            userRepository.save(user);
            
            notificationService.sendNotification(
                notificationService.createNotification("email", userId, 
                    "A new diet plan has been assigned to you: " + plan.getName())
            );
            
            return plan;
        }
        
        throw new RuntimeException("Diet plan or user not found");
    }
}