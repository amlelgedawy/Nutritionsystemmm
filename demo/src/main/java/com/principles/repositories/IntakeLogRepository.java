package com.principles.repositories;

import com.principles.models.IntakeLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface IntakeLogRepository extends MongoRepository<IntakeLog, String> {
    List<IntakeLog> findByUserId(String userId);
    List<IntakeLog> findByUserIdAndMealType(String userId, String mealType);
    
    @Query("{'userId': ?0, 'loggedAt': {$gte: ?1, $lte: ?2}}")
    List<IntakeLog> findByUserIdAndDateRange(String userId, Date startDate, Date endDate);
    
    @Query("{'loggedAt': {$gte: ?0, $lte: ?1}}")
    List<IntakeLog> findByDateRange(Date startDate, Date endDate);
}