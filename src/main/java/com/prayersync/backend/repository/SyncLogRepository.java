package com.prayersync.backend.repository;

import com.prayersync.backend.entity.SyncLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SyncLogRepository extends JpaRepository<SyncLog, Long> {
    
    List<SyncLog> findByUserIdOrderByCreatedAtDesc(String userId);
    
    Optional<SyncLog> findFirstByUserIdAndStatusOrderByCreatedAtDesc(String userId, String status);
    
    @Query("SELECT s FROM SyncLog s WHERE s.userId = :userId AND s.createdAt >= :since ORDER BY s.createdAt DESC")
    List<SyncLog> findByUserIdSince(@Param("userId") String userId, @Param("since") LocalDateTime since);
    
    @Query("SELECT s FROM SyncLog s WHERE s.status = :status ORDER BY s.createdAt DESC")
    List<SyncLog> findByStatus(@Param("status") String status);
    
    long countByUserIdAndStatus(String userId, String status);
}