package com.principles.services;

import com.principles.models.FoodItem;
import com.principles.repositories.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    @Autowired
    private FoodItemRepository foodItemRepository;
    
    @Autowired
    private ExternalNutritionAdapter externalNutritionAdapter;

    public List<FoodItem> findAll() {
        return foodItemRepository.findAll();
    }
    
    public Optional<FoodItem> findById(String id) {
        return foodItemRepository.findById(id);
    }
    
    public List<FoodItem> searchByName(String name) {
        return foodItemRepository.findByNameContainingIgnoreCase(name);
    }
    
    public List<FoodItem> findByCategory(String category) {
        return foodItemRepository.findByCategoriesContaining(category);
    }
    
    public List<FoodItem> findVerifiedFoods() {
        return foodItemRepository.findByIsVerified(true);
    }
    
    public List<FoodItem> findByCalorieRange(double minCalories, double maxCalories) {
        return foodItemRepository.findByCalorieRange(minCalories, maxCalories);
    }
    
    public List<FoodItem> findHighProteinFoods(double minProtein) {
        return foodItemRepository.findHighProteinFoods(minProtein);
    }
    
    public FoodItem addFoodItem(FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }
    
    public FoodItem updateFoodItem(FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }
    
    public void deleteFoodItem(String id) {
        foodItemRepository.deleteById(id);
    }
    
    public FoodItem searchExternalFoodData(String foodName) {
        return externalNutritionAdapter.searchFoodData(foodName);
    }
    
    public FoodItem addExternalFoodToDatabase(String foodName) {
        FoodItem externalFood = externalNutritionAdapter.searchFoodData(foodName);
        externalFood.setVerified(false); // Needs admin verification
        return foodItemRepository.save(externalFood);
    }
}