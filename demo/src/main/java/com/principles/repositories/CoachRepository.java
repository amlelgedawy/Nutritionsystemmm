package com.principles.repositories;

import com.principles.models.Coach;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CoachRepository extends MongoRepository<Coach, String> {
    Optional<Coach> findByUsername(String username);
    Optional<Coach> findByEmail(String email);
    List<Coach> findByIsActive(boolean isActive);
    List<Coach> findBySpecialization(String specialization);
    
    @Query("{'experienceYears': {$gte: ?0}}")
    List<Coach> findByMinimumExperience(int minYears);
    
    @Query("{'clientIds': {$size: {$lt: ?0}}}")
    List<Coach> findAvailableCoaches(int maxClients);
}