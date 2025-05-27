package com.principles.controllers;

import com.principles.models.Feedback;
import com.principles.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @PostMapping
    public ResponseEntity<Feedback> submitFeedback(@RequestBody Feedback feedback) {
        Feedback savedFeedback = feedbackRepository.save(feedback);
        return ResponseEntity.ok(savedFeedback);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Feedback>> getUserFeedback(@PathVariable String userId) {
        List<Feedback> feedback = feedbackRepository.findByUserId(userId);
        return ResponseEntity.ok(feedback);
    }
    
    @GetMapping("/coach/{coachId}")
    public ResponseEntity<List<Feedback>> getCoachFeedback(@PathVariable String coachId) {
        List<Feedback> feedback = feedbackRepository.findByCoachId(coachId);
        return ResponseEntity.ok(feedback);
    }
    
    @GetMapping("/{feedbackId}")
    public ResponseEntity<Feedback> getFeedback(@PathVariable String feedbackId) {
        Optional<Feedback> feedbackOpt = feedbackRepository.findById(feedbackId);
        return feedbackOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Feedback>> getFeedbackByStatus(@PathVariable String status) {
        List<Feedback> feedback = feedbackRepository.findByStatus(status);
        return ResponseEntity.ok(feedback);
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Feedback>> getFeedbackByType(@PathVariable String type) {
        List<Feedback> feedback = feedbackRepository.findByType(type);
        return ResponseEntity.ok(feedback);
    }
}