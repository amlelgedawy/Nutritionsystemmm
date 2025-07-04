package com.principles.repositories;

import com.principles.models.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends MongoRepository<Admin, String> {
    Optional<Admin> findByUsername(String username);
    Optional<Admin> findByEmail(String email);
    List<Admin> findByIsActive(boolean isActive);
}