package com.prayersync.backend.controller;

import com.prayersync.backend.dto.SyncRequestDto;
import com.prayersync.backend.dto.SyncResponseDto;
import com.prayersync.backend.service.SyncService;
import com.prayersync.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/sync")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SyncController {

    @Autowired
    private SyncService syncService;
    
    @Autowired
    private UserService userService;

    @PostMapping("/bidirectional")
    public ResponseEntity<SyncResponseDto> bidirectionalSync(
            @Valid @RequestBody SyncRequestDto request,
            Authentication authentication) {
        
        String userId = getUserIdFromAuth(authentication);
        SyncResponseDto response = syncService.performBidirectionalSync(request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/changes/{timestamp}")
    public ResponseEntity<SyncResponseDto> getChangesSince(
            @PathVariable("timestamp") String timestamp,
            Authentication authentication) {
        
        String userId = getUserIdFromAuth(authentication);
        LocalDateTime since = LocalDateTime.parse(timestamp);
        SyncResponseDto response = syncService.getChangesSince(since, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<SyncResponseDto> uploadChanges(
            @Valid @RequestBody SyncRequestDto request,
            Authentication authentication) {
        
        System.out.println("🔄 SYNC: Upload endpoint called");
        String userId = getUserIdFromAuth(authentication);
        System.out.println("🔑 SYNC: User ID: " + userId);
        System.out.println("📦 SYNC: Request data - Prayer Requests: " + 
            (request.getUpdatedPrayerRequests() != null ? request.getUpdatedPrayerRequests().size() : 0) +
            ", Prayer Lists: " + 
            (request.getUpdatedPrayerLists() != null ? request.getUpdatedPrayerLists().size() : 0));
        
        SyncResponseDto response = syncService.uploadChanges(request, userId);
        System.out.println("✅ SYNC: Upload completed successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/server-time")
    public ResponseEntity<LocalDateTime> getServerTime() {
        return ResponseEntity.ok(LocalDateTime.now());
    }
    
    private String getUserIdFromAuth(Authentication authentication) {
        String email = authentication.getName(); // JWT contains email as subject
        return userService.getUserByEmail(email).getId(); // Get user ID from email
    }
}