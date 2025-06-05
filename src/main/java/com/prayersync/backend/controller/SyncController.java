package com.prayersync.backend.controller;

import com.prayersync.backend.dto.PrayerListDto;
import com.prayersync.backend.dto.PrayerRequestDto;
import com.prayersync.backend.entity.PrayerList;
import com.prayersync.backend.entity.PrayerRequest;
import com.prayersync.backend.security.CustomUserDetails;
import com.prayersync.backend.service.SyncService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sync")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SyncController {

    @Autowired
    private SyncService syncService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/download")
    public ResponseEntity<SyncDownloadResponse> downloadChanges(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastSyncTime,
            Authentication authentication) {
        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = userDetails.getId();

        SyncService.SyncResult result = syncService.downloadChanges(userId, lastSyncTime);

        // Convert entities to DTOs
        List<PrayerRequestDto> updatedRequestDtos = result.getUpdatedRequests().stream()
                .map(request -> modelMapper.map(request, PrayerRequestDto.class))
                .collect(Collectors.toList());

        List<PrayerListDto> updatedListDtos = result.getUpdatedLists().stream()
                .map(list -> modelMapper.map(list, PrayerListDto.class))
                .collect(Collectors.toList());

        List<PrayerRequestDto> deletedRequestDtos = result.getDeletedRequests().stream()
                .map(request -> modelMapper.map(request, PrayerRequestDto.class))
                .collect(Collectors.toList());

        List<PrayerListDto> deletedListDtos = result.getDeletedLists().stream()
                .map(list -> modelMapper.map(list, PrayerListDto.class))
                .collect(Collectors.toList());

        SyncDownloadResponse response = new SyncDownloadResponse(
                updatedRequestDtos, updatedListDtos, deletedRequestDtos, deletedListDtos, LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<SyncUploadResponse> uploadChanges(
            @RequestBody SyncUploadRequest request,
            Authentication authentication) {
        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = userDetails.getId();

        // Convert DTOs to entities
        List<PrayerRequest> requests = request.getPrayerRequests().stream()
                .map(dto -> modelMapper.map(dto, PrayerRequest.class))
                .collect(Collectors.toList());

        List<PrayerList> lists = request.getPrayerLists().stream()
                .map(dto -> modelMapper.map(dto, PrayerList.class))
                .collect(Collectors.toList());

        syncService.uploadChanges(userId, requests, lists);

        SyncUploadResponse response = new SyncUploadResponse(
                "Upload completed successfully", 
                LocalDateTime.now(),
                requests.size() + lists.size());

        return ResponseEntity.ok(response);
    }

    // DTOs for sync responses
    public static class SyncDownloadResponse {
        private List<PrayerRequestDto> updatedRequests;
        private List<PrayerListDto> updatedLists;
        private List<PrayerRequestDto> deletedRequests;
        private List<PrayerListDto> deletedLists;
        private LocalDateTime syncTime;

        public SyncDownloadResponse(List<PrayerRequestDto> updatedRequests, List<PrayerListDto> updatedLists,
                                   List<PrayerRequestDto> deletedRequests, List<PrayerListDto> deletedLists,
                                   LocalDateTime syncTime) {
            this.updatedRequests = updatedRequests;
            this.updatedLists = updatedLists;
            this.deletedRequests = deletedRequests;
            this.deletedLists = deletedLists;
            this.syncTime = syncTime;
        }

        // Getters
        public List<PrayerRequestDto> getUpdatedRequests() { return updatedRequests; }
        public List<PrayerListDto> getUpdatedLists() { return updatedLists; }
        public List<PrayerRequestDto> getDeletedRequests() { return deletedRequests; }
        public List<PrayerListDto> getDeletedLists() { return deletedLists; }
        public LocalDateTime getSyncTime() { return syncTime; }
    }

    public static class SyncUploadRequest {
        private List<PrayerRequestDto> prayerRequests;
        private List<PrayerListDto> prayerLists;

        // Getters and setters
        public List<PrayerRequestDto> getPrayerRequests() { return prayerRequests; }
        public void setPrayerRequests(List<PrayerRequestDto> prayerRequests) { this.prayerRequests = prayerRequests; }
        public List<PrayerListDto> getPrayerLists() { return prayerLists; }
        public void setPrayerLists(List<PrayerListDto> prayerLists) { this.prayerLists = prayerLists; }
    }

    public static class SyncUploadResponse {
        private String message;
        private LocalDateTime syncTime;
        private int itemsProcessed;

        public SyncUploadResponse(String message, LocalDateTime syncTime, int itemsProcessed) {
            this.message = message;
            this.syncTime = syncTime;
            this.itemsProcessed = itemsProcessed;
        }

        // Getters
        public String getMessage() { return message; }
        public LocalDateTime getSyncTime() { return syncTime; }
        public int getItemsProcessed() { return itemsProcessed; }
    }
}