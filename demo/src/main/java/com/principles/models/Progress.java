package com.principles.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "progress")
public class Progress {
    @Id
    private String id;
    private String userId;
    private double weight;
    private double bodyFatPercentage;
    private double muscleMass;
    private double waistCircumference;
    private double chestCircumference;
    private double armCircumference;
    private double thighCircumference;
    private Date recordedAt;
    private String notes;

    public Progress() {
        this.recordedAt = new Date();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    
    public double getBodyFatPercentage() { return bodyFatPercentage; }
    public void setBodyFatPercentage(double bodyFatPercentage) { this.bodyFatPercentage = bodyFatPercentage; }
    
    public double getMuscleMass() { return muscleMass; }
    public void setMuscleMass(double muscleMass) { this.muscleMass = muscleMass; }
    
    public double getWaistCircumference() { return waistCircumference; }
    public void setWaistCircumference(double waistCircumference) { this.waistCircumference = waistCircumference; }
    
    public double getChestCircumference() { return chestCircumference; }
    public void setChestCircumference(double chestCircumference) { this.chestCircumference = chestCircumference; }
    
    public double getArmCircumference() { return armCircumference; }
    public void setArmCircumference(double armCircumference) { this.armCircumference = armCircumference; }
    
    public double getThighCircumference() { return thighCircumference; }
    public void setThighCircumference(double thighCircumference) { this.thighCircumference = thighCircumference; }
    
    public Date getRecordedAt() { return recordedAt; }
    public void setRecordedAt(Date recordedAt) { this.recordedAt = recordedAt; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}