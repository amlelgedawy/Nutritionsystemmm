package com.principles.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "fooditems")
public class FoodItem {
    @Id
    private String id;
    private String name;
    private double calories;
    private double protein;
    private double carbs;
    private double fat;
    private double fiber;
    private List<String> categories;
    private boolean isVerified;

    public FoodItem() {
        this.categories = new ArrayList<>();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }
    
    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }
    
    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
    
    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }
    
    public double getFiber() { return fiber; }
    public void setFiber(double fiber) { this.fiber = fiber; }
    
    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }
    
    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
}