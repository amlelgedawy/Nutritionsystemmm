package com.principles.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "coaches")
public class Coach {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private String name;
    private String specialization;
    private int experienceYears;
    private String certification;
    private List<String> clientIds;
    private boolean isActive;

    public Coach() {
        this.clientIds = new ArrayList<>();
        this.isActive = true;
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
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    
    public String getCertification() { return certification; }
    public void setCertification(String certification) { this.certification = certification; }
    
    public List<String> getClientIds() { return clientIds; }
    public void setClientIds(List<String> clientIds) { this.clientIds = clientIds; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}