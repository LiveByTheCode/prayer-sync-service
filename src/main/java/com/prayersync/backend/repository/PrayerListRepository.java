package com.prayersync.backend.repository;

import com.prayersync.backend.entity.PrayerList;
import com.prayersync.backend.enums.PrivacyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrayerListRepository extends JpaRepository<PrayerList, String> {
    
    List<PrayerList> findByCreatorId(String creatorId);
    
    List<PrayerList> findByChurchId(String churchId);
    
    List<PrayerList> findByIsActiveTrue();
    
    List<PrayerList> findByPrivacyLevel(PrivacyLevel privacyLevel);
    
    @Query("SELECT pl FROM PrayerList pl WHERE pl.church.id = :churchId AND pl.isActive = true")
    List<PrayerList> findActiveByChurch(@Param("churchId") String churchId);
    
    @Query("SELECT pl FROM PrayerList pl WHERE pl.creator.id = :creatorId AND pl.isActive = true")
    List<PrayerList> findActiveByCreator(@Param("creatorId") String creatorId);
    
    @Query("SELECT pl FROM PrayerList pl WHERE pl.privacyLevel = 'PUBLIC' AND pl.isActive = true " +
           "ORDER BY pl.updatedAt DESC")
    List<PrayerList> findPublicPrayerLists();
    
    @Query("SELECT pl FROM PrayerList pl WHERE " +
           "(pl.privacyLevel = 'PUBLIC' OR " +
           "(pl.privacyLevel = 'CHURCH' AND pl.church.id = :churchId) OR " +
           "pl.creator.id = :userId) AND pl.isActive = true " +
           "ORDER BY pl.updatedAt DESC")
    List<PrayerList> findAccessiblePrayerLists(@Param("userId") String userId, @Param("churchId") String churchId);
    
    @Query("SELECT pl FROM PrayerList pl WHERE LOWER(pl.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "AND pl.isActive = true")
    List<PrayerList> searchByName(@Param("searchTerm") String searchTerm);
}