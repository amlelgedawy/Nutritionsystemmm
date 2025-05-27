package com.principles.services;

import com.principles.models.FoodItem;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// Adapter Pattern - Adapts external nutrition API to our system
@Service
public class ExternalNutritionAdapter {

    // Target interface that our system expects
    public interface NutritionDataProvider {
        FoodItem getFoodData(String foodName);
    }
    
    // Adaptee - External API client (simulated)
    private class ExternalNutritionAPI {
        private RestTemplate restTemplate = new RestTemplate();
        
        public ExternalFoodData getFoodNutritionData(String query) {
            // In a real implementation, this would call an external API
            // For demonstration, we'll create a mock response
            ExternalFoodData data = new ExternalFoodData();
            data.name = query;
            data.nutritionPer100g = new NutritionData();
            data.nutritionPer100g.energy = 250;
            data.nutritionPer100g.proteins = 10;
            data.nutritionPer100g.carbohydrates = 30;
            data.nutritionPer100g.fats = 8;
            data.nutritionPer100g.fiber = 3;
            return data;
        }
        
        // External API data structure
        private class ExternalFoodData {
            public String name;
            public NutritionData nutritionPer100g;
        }
        
        private class NutritionData {
            public double energy;
            public double proteins;
            public double carbohydrates;
            public double fats;
            public double fiber;
        }
    }
    
    // Adapter - Adapts ExternalNutritionAPI to NutritionDataProvider
    private class NutritionAPIAdapter implements NutritionDataProvider {
        private ExternalNutritionAPI externalAPI;
        
        public NutritionAPIAdapter() {
            this.externalAPI = new ExternalNutritionAPI();
        }
        
        @Override
        public FoodItem getFoodData(String foodName) {
            // Call the external API
            ExternalNutritionAPI.ExternalFoodData externalData = externalAPI.getFoodNutritionData(foodName);
            
            // Convert to our internal FoodItem model
            FoodItem foodItem = new FoodItem();
            foodItem.setName(externalData.name);
            foodItem.setCalories(externalData.nutritionPer100g.energy);
            foodItem.setProtein(externalData.nutritionPer100g.proteins);
            foodItem.setCarbs(externalData.nutritionPer100g.carbohydrates);
            foodItem.setFat(externalData.nutritionPer100g.fats);
            foodItem.setFiber(externalData.nutritionPer100g.fiber);
            foodItem.setVerified(true);
            
            return foodItem;
        }
    }
    
    private NutritionDataProvider adapter;
    
    public ExternalNutritionAdapter() {
        this.adapter = new NutritionAPIAdapter();
    }
    
    public FoodItem searchFoodData(String foodName) {
        return adapter.getFoodData(foodName);
    }
}