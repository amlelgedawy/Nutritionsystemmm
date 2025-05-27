package com.principles.repositories;

import com.principles.models.FoodItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodItemRepository extends MongoRepository<FoodItem, String> {
    List<FoodItem> findByNameContainingIgnoreCase(String name);
    List<FoodItem> findByCategoriesContaining(String category);
    List<FoodItem> findByIsVerified(boolean isVerified);
    
    @Query("{'calories': {$gte: ?0, $lte: ?1}}")
    List<FoodItem> findByCalorieRange(double minCalories, double maxCalories);
    
    @Query("{'protein': {$gte: ?0}}")
    List<FoodItem> findHighProteinFoods(double minProtein);
}