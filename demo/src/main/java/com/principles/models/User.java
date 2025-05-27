package com.principles.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private String name;
    private int age;
    private double height;
    private double weight;
    private String gender;
    private double activityLevel;
    private List<Progress> progressHistory;
    private List<IntakeLog> intakeLogs;
    private DietPlan currentDietPlan;
    private String coachId;
    private String role = "USER";

    public User() {
        this.progressHistory = new ArrayList<>();
        this.intakeLogs = new ArrayList<>();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public double getActivityLevel() { return activityLevel; }
    public void setActivityLevel(double activityLevel) { this.activityLevel = activityLevel; }
    
    public List<Progress> getProgressHistory() { return progressHistory; }
    public void setProgressHistory(List<Progress> progressHistory) { this.progressHistory = progressHistory; }
    
    public List<IntakeLog> getIntakeLogs() { return intakeLogs; }
    public void setIntakeLogs(List<IntakeLog> intakeLogs) { this.intakeLogs = intakeLogs; }
    
    public DietPlan getCurrentDietPlan() { return currentDietPlan; }
    public void setCurrentDietPlan(DietPlan currentDietPlan) { this.currentDietPlan = currentDietPlan; }
    
    public String getCoachId() { return coachId; }
    public void setCoachId(String coachId) { this.coachId = coachId; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}