package com.principles.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.principles.models.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByCoachId(String coachId);
    
    @Query("{'age': {$gte: ?0, $lte: ?1}}")
    List<User> findByAgeRange(int minAge, int maxAge);
    
    @Query("{'weight': {$gte: ?0, $lte: ?1}}")
    List<User> findByWeightRange(double minWeight, double maxWeight);
}