package com.prayersync.backend.repository;

import com.prayersync.backend.entity.Church;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChurchRepository extends JpaRepository<Church, String> {
    
    @Query("SELECT c FROM Church c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Church> searchByName(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT c FROM Church c WHERE LOWER(c.address) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<Church> findByLocationContaining(@Param("location") String location);
    
    @Query("SELECT c FROM Church c ORDER BY c.createdAt DESC")
    List<Church> findAllOrderByCreatedAtDesc();
}