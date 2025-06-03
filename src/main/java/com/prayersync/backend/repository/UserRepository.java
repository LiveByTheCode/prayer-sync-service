package com.prayersync.backend.repository;

import com.prayersync.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<User> findByChurchId(String churchId);
    
    List<User> findByIsActiveTrue();
    
    @Query("SELECT u FROM User u WHERE u.church.id = :churchId AND u.isActive = true")
    List<User> findActiveUsersByChurch(@Param("churchId") String churchId);
    
    @Query("SELECT u FROM User u WHERE u.church.id = :churchId AND u.isChurchAdmin = true")
    List<User> findChurchAdminsByChurch(@Param("churchId") String churchId);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> searchUsers(@Param("searchTerm") String searchTerm);
}