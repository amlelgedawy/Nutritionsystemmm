package com.principles.repositories;

import com.principles.models.Progress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface ProgressRepository extends MongoRepository<Progress, String> {
    List<Progress> findByUserId(String userId);
    
    @Query("{'userId': ?0, 'recordedAt': {$gte: ?1, $lte: ?2}}")
    List<Progress> findByUserIdAndDateRange(String userId, Date startDate, Date endDate);
    
    @Query("{'userId': ?0}")
    List<Progress> findByUserIdOrderByRecordedAtDesc(String userId);
}