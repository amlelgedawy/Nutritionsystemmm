package com.principles.repositories;

import com.principles.models.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface FeedbackRepository extends MongoRepository<Feedback, String> {
    List<Feedback> findByUserId(String userId);
    List<Feedback> findByCoachId(String coachId);
    List<Feedback> findByStatus(String status);
    List<Feedback> findByType(String type);
    
    @Query("{'createdAt': {$gte: ?0, $lte: ?1}}")
    List<Feedback> findByDateRange(Date startDate, Date endDate);
    
    @Query("{'status': 'pending'}")
    List<Feedback> findPendingFeedback();
}