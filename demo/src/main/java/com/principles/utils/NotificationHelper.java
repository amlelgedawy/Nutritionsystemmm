package com.principles.utils;

import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class NotificationHelper {
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public String formatNotificationMessage(String template, Object... args) {
        return String.format(template, args);
    }
    
    public String getCurrentTimestamp() {
        return dateFormat.format(new Date());
    }
    
    public String createWelcomeMessage(String userName) {
        return formatNotificationMessage(
            "Welcome to NutritionX, %s! We're excited to help you on your nutrition journey.", 
            userName
        );
    }
    
    public String createDietPlanAssignedMessage(String planName) {
        return formatNotificationMessage(
            "A new diet plan '%s' has been assigned to you. Check your dashboard for details.", 
            planName
        );
    }
    
    public String createProgressReminderMessage() {
        return "Don't forget to log your progress today! Regular tracking helps you achieve your goals.";
    }
    
    public String createMealReminderMessage(String mealType) {
        return formatNotificationMessage(
            "Time for your %s! Remember to log your food intake.", 
            mealType
        );
    }
    
    public String createCoachAssignedMessage(String coachName) {
        return formatNotificationMessage(
            "You have been assigned a coach: %s. They will help guide your nutrition journey.", 
            coachName
        );
    }
    
    public String createFeedbackResponseMessage() {
        return "Your feedback has been reviewed and responded to. Thank you for helping us improve!";
    }
}