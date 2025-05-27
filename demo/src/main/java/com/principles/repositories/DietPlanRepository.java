package com.principles.repositories;

import com.principles.models.DietPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface DietPlanRepository extends MongoRepository<DietPlan, String> {
    List<DietPlan> findByUserId(String userId);
    List<DietPlan> findByCoachId(String coachId);
    
    @Query("{'startDate': {$lte: ?0}, 'endDate': {$gte: ?0}}")
    List<DietPlan> findActivePlans(Date currentDate);
    
    @Query("{'dailyCalorieTarget': {$gte: ?0, $lte: ?1}}")
    List<DietPlan> findByCalorieRange(int minCalories, int maxCalories);
}