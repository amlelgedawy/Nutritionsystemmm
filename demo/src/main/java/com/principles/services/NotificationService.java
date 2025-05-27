package com.principles.services;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

// Observer Pattern
@Service
public class NotificationService {
    
    // Singleton Pattern (Spring manages this as a singleton by default)
    private static final NotificationService instance = new NotificationService();
    
    public static NotificationService getInstance() {
        return instance;
    }
    
    // Observer Pattern - Subject
    private List<NotificationObserver> observers = new ArrayList<>();
    
    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyAll(String userId, String message, String type) {
        for (NotificationObserver observer : observers) {
            observer.update(userId, message, type);
        }
    }
    
    // Factory Method Pattern
    public Notification createNotification(String type, String userId, String message) {
        switch (type) {
            case "email":
                return new EmailNotification(userId, message);
            case "push":
                return new PushNotification(userId, message);
            case "sms":
                return new SMSNotification(userId, message);
            default:
                return new InAppNotification(userId, message);
        }
    }
    
    public void sendNotification(Notification notification) {
        notification.send();
        notifyAll(notification.getUserId(), notification.getMessage(), notification.getType());
    }
    
    // Observer Pattern - Observer Interface
    public interface NotificationObserver {
        void update(String userId, String message, String type);
    }
    
    // Factory Method Pattern - Product
    public interface Notification {
        void send();
        String getUserId();
        String getMessage();
        String getType();
    }
    
    // Concrete Products
    private class EmailNotification implements Notification {
        private String userId;
        private String message;
        
        public EmailNotification(String userId, String message) {
            this.userId = userId;
            this.message = message;
        }
        
        @Override
        public void send() {
            System.out.println("Sending email notification to user: " + userId);
            System.out.println("Message: " + message);
            // Email sending logic would go here
        }
        
        @Override
        public String getUserId() { return userId; }
        
        @Override
        public String getMessage() { return message; }
        
        @Override
        public String getType() { return "email"; }
    }
    
    private class PushNotification implements Notification {
        private String userId;
        private String message;
        
        public PushNotification(String userId, String message) {
            this.userId = userId;
            this.message = message;
        }
        
        @Override
        public void send() {
            System.out.println("Sending push notification to user: " + userId);
            System.out.println("Message: " + message);
            // Push notification logic would go here
        }
        
        @Override
        public String getUserId() { return userId; }
        
        @Override
        public String getMessage() { return message; }
        
        @Override
        public String getType() { return "push"; }
    }
    
    private class SMSNotification implements Notification {
        private String userId;
        private String message;
        
        public SMSNotification(String userId, String message) {
            this.userId = userId;
            this.message = message;
        }
        
        @Override
        public void send() {
            System.out.println("Sending SMS notification to user: " + userId);
            System.out.println("Message: " + message);
            // SMS sending logic would go here
        }
        
        @Override
        public String getUserId() { return userId; }
        
        @Override
        public String getMessage() { return message; }
        
        @Override
        public String getType() { return "sms"; }
    }
    
    private class InAppNotification implements Notification {
        private String userId;
        private String message;
        
        public InAppNotification(String userId, String message) {
            this.userId = userId;
            this.message = message;
        }
        
        @Override
        public void send() {
            System.out.println("Sending in-app notification to user: " + userId);
            System.out.println("Message: " + message);
            // In-app notification logic would go here
        }
        
        @Override
        public String getUserId() { return userId; }
        
        @Override
        public String getMessage() { return message; }
        
        @Override
        public String getType() { return "in-app"; }
    }
}