package com.principles.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

@Document(collection = "dietplans")
public class DietPlan {
    @Id
    private String id;
    private String name;
    private String userId;
    private String coachId;
    private Date startDate;
    private Date endDate;
    private int dailyCalorieTarget;
    private int proteinTarget;
    private int carbsTarget;
    private int fatTarget;
    private List<FoodItem> recommendedFoods;
    private List<String> dietaryRestrictions;
    private String notes;

    public DietPlan() {
        this.recommendedFoods = new ArrayList<>();
        this.dietaryRestrictions = new ArrayList<>();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getCoachId() { return coachId; }
    public void setCoachId(String coachId) { this.coachId = coachId; }
    
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    
    public int getDailyCalorieTarget() { return dailyCalorieTarget; }
    public void setDailyCalorieTarget(int dailyCalorieTarget) { this.dailyCalorieTarget = dailyCalorieTarget; }
    
    public int getProteinTarget() { return proteinTarget; }
    public void setProteinTarget(int proteinTarget) { this.proteinTarget = proteinTarget; }
    
    public int getCarbsTarget() { return carbsTarget; }
    public void setCarbsTarget(int carbsTarget) { this.carbsTarget = carbsTarget; }
    
    public int getFatTarget() { return fatTarget; }
    public void setFatTarget(int fatTarget) { this.fatTarget = fatTarget; }
    
    public List<FoodItem> getRecommendedFoods() { return recommendedFoods; }
    public void setRecommendedFoods(List<FoodItem> recommendedFoods) { this.recommendedFoods = recommendedFoods; }
    
    public List<String> getDietaryRestrictions() { return dietaryRestrictions; }
    public void setDietaryRestrictions(List<String> dietaryRestrictions) { this.dietaryRestrictions = dietaryRestrictions; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    // Builder Pattern implementation
    public static class Builder {
        private DietPlan plan;
        
        public Builder() {
            plan = new DietPlan();
        }
        
        public Builder withName(String name) {
            plan.name = name;
            return this;
        }
        
        public Builder forUser(String userId) {
            plan.userId = userId;
            return this;
        }
        
        public Builder byCoach(String coachId) {
            plan.coachId = coachId;
            return this;
        }
        
        public Builder withDuration(Date startDate, Date endDate) {
            plan.startDate = startDate;
            plan.endDate = endDate;
            return this;
        }
        
        public Builder withCalorieTarget(int calories) {
            plan.dailyCalorieTarget = calories;
            return this;
        }
        
        public Builder withMacros(int protein, int carbs, int fat) {
            plan.proteinTarget = protein;
            plan.carbsTarget = carbs;
            plan.fatTarget = fat;
            return this;
        }
        
        public Builder withRecommendedFoods(List<FoodItem> foods) {
            plan.recommendedFoods = foods;
            return this;
        }
        
        public Builder withRestrictions(List<String> restrictions) {
            plan.dietaryRestrictions = restrictions;
            return this;
        }
        
        public Builder withNotes(String notes) {
            plan.notes = notes;
            return this;
        }
        
        public DietPlan build() {
            return plan;
        }
    }
}