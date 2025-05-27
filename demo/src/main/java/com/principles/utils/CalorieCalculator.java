package com.principles.utils;

import com.principles.models.User;
import org.springframework.stereotype.Component;

// Strategy Pattern
@Component
public class CalorieCalculator {
    
    // Strategy Pattern - Strategy Interface
    public interface CalorieCalculationStrategy {
        double calculateBMR(User user);
        double calculateTDEE(User user);
    }
    
    // Concrete Strategies
    public static class HarrisBenedictStrategy implements CalorieCalculationStrategy {
        @Override
        public double calculateBMR(User user) {
            if (user.getGender().equalsIgnoreCase("male")) {
                return 88.362 + (13.397 * user.getWeight()) + (4.799 * user.getHeight()) - (5.677 * user.getAge());
            } else {
                return 447.593 + (9.247 * user.getWeight()) + (3.098 * user.getHeight()) - (4.330 * user.getAge());
            }
        }
        
        @Override
        public double calculateTDEE(User user) {
            return calculateBMR(user) * user.getActivityLevel();
        }
    }
    
    public static class MifflinStJeorStrategy implements CalorieCalculationStrategy {
        @Override
        public double calculateBMR(User user) {
            if (user.getGender().equalsIgnoreCase("male")) {
                return (10 * user.getWeight()) + (6.25 * user.getHeight()) - (5 * user.getAge()) + 5;
            } else {
                return (10 * user.getWeight()) + (6.25 * user.getHeight()) - (5 * user.getAge()) - 161;
            }
        }
        
        @Override
        public double calculateTDEE(User user) {
            return calculateBMR(user) * user.getActivityLevel();
        }
    }
    
    public static class KatchMcArdleStrategy implements CalorieCalculationStrategy {
        @Override
        public double calculateBMR(User user) {
            // This would require body fat percentage which we don't have in our User model
            // For demonstration purposes, we'll use a simplified version
            return 370 + (21.6 * (user.getWeight() * 0.8)); // Assuming lean body mass is 80% of total weight
        }
        
        @Override
        public double calculateTDEE(User user) {
            return calculateBMR(user) * user.getActivityLevel();
        }
    }
    
    // Context
    private CalorieCalculationStrategy strategy;
    
    public CalorieCalculator() {
        // Default strategy
        this.strategy = new HarrisBenedictStrategy();
    }
    
    public void setStrategy(CalorieCalculationStrategy strategy) {
        this.strategy = strategy;
    }
    
    public double calculateDailyCalories(User user) {
        return strategy.calculateTDEE(user);
    }
    
    public double calculateWeightLossCalories(User user, double weightLossPerWeek) {
        // 1 kg of fat is approximately 7700 calories
        double dailyDeficit = (weightLossPerWeek * 7700) / 7;
        return strategy.calculateTDEE(user) - dailyDeficit;
    }
    
    public double calculateWeightGainCalories(User user, double weightGainPerWeek) {
        // 1 kg of muscle requires approximately 2500-3500 calories surplus
        double dailySurplus = (weightGainPerWeek * 3000) / 7;
        return strategy.calculateTDEE(user) + dailySurplus;
    }
}