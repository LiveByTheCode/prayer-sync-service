package com.prayersync.backend.repository;

import com.prayersync.backend.entity.PrayerRequest;
import com.prayersync.backend.enums.RequestCategory;
import com.prayersync.backend.enums.RequestPriority;
import com.prayersync.backend.enums.RequestStatus;
import com.prayersync.backend.enums.PrivacyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PrayerRequestRepository extends JpaRepository<PrayerRequest, String> {
    
    List<PrayerRequest> findByCreatorId(String creatorId);
    
    List<PrayerRequest> findByPrayerListId(String prayerListId);
    
    List<PrayerRequest> findByStatus(RequestStatus status);
    
    List<PrayerRequest> findByCategory(RequestCategory category);
    
    List<PrayerRequest> findByPriority(RequestPriority priority);
    
    List<PrayerRequest> findByPrivacyLevel(PrivacyLevel privacyLevel);
    
    @Query("SELECT pr FROM PrayerRequest pr WHERE pr.status = 'ACTIVE' ORDER BY pr.priority DESC, pr.createdAt DESC")
    List<PrayerRequest> findActivePrayerRequests();
    
    @Query("SELECT pr FROM PrayerRequest pr WHERE pr.prayerList.church.id = :churchId AND pr.status = 'ACTIVE' " +
           "ORDER BY pr.priority DESC, pr.createdAt DESC")
    List<PrayerRequest> findActiveByChurch(@Param("churchId") String churchId);
    
    @Query("SELECT pr FROM PrayerRequest pr WHERE pr.creator.id = :creatorId AND pr.status = 'ACTIVE' " +
           "ORDER BY pr.priority DESC, pr.createdAt DESC")
    List<PrayerRequest> findActiveByCreator(@Param("creatorId") String creatorId);
    
    @Query("SELECT pr FROM PrayerRequest pr WHERE pr.privacyLevel = 'PUBLIC' AND pr.status = 'ACTIVE' " +
           "ORDER BY pr.priority DESC, pr.createdAt DESC")
    List<PrayerRequest> findPublicPrayerRequests();
    
    @Query("SELECT pr FROM PrayerRequest pr WHERE " +
           "(pr.privacyLevel = 'PUBLIC' OR " +
           "(pr.privacyLevel = 'CHURCH' AND pr.prayerList.church.id = :churchId) OR " +
           "pr.creator.id = :userId) AND pr.status = 'ACTIVE' " +
           "ORDER BY pr.priority DESC, pr.createdAt DESC")
    List<PrayerRequest> findAccessiblePrayerRequests(@Param("userId") String userId, @Param("churchId") String churchId);
    
    @Query("SELECT pr FROM PrayerRequest pr WHERE LOWER(pr.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(pr.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<PrayerRequest> searchByTitleOrDescription(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT pr FROM PrayerRequest pr WHERE pr.status = 'ANSWERED' AND pr.answeredAt >= :fromDate " +
           "ORDER BY pr.answeredAt DESC")
    List<PrayerRequest> findAnsweredSince(@Param("fromDate") LocalDateTime fromDate);
    
    @Query("SELECT pr FROM PrayerRequest pr WHERE pr.priority = 'HIGH' AND pr.status = 'ACTIVE' " +
           "ORDER BY pr.createdAt DESC")
    List<PrayerRequest> findUrgentPrayerRequests();
    
    @Query("SELECT COUNT(pr) FROM PrayerRequest pr WHERE pr.creator.id = :userId")
    Long countByCreator(@Param("userId") String userId);
    
    @Query("SELECT COUNT(pr) FROM PrayerRequest pr WHERE pr.prayerList.church.id = :churchId")
    Long countByChurch(@Param("churchId") String churchId);
}