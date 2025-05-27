package com.principles.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "intake_logs")
public class IntakeLog {
    @Id
    private String id;
    private String userId;
    private String foodItemId;
    private String foodName;
    private double quantity;
    private String unit;
    private double calories;
    private double protein;
    private double carbs;
    private double fat;
    private Date loggedAt;
    private String mealType; // breakfast, lunch, dinner, snack

    public IntakeLog() {
        this.loggedAt = new Date();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getFoodItemId() { return foodItemId; }
    public void setFoodItemId(String foodItemId) { this.foodItemId = foodItemId; }
    
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }
    
    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }
    
    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
    
    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }
    
    public Date getLoggedAt() { return loggedAt; }
    public void setLoggedAt(Date loggedAt) { this.loggedAt = loggedAt; }
    
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
}