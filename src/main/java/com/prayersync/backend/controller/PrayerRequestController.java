package com.prayersync.backend.controller;

import com.prayersync.backend.dto.CreatePrayerRequestDto;
import com.prayersync.backend.dto.PrayerRequestDto;
import com.prayersync.backend.security.CustomUserDetails;
import com.prayersync.backend.service.PrayerRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prayer-requests")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PrayerRequestController {

    @Autowired
    private PrayerRequestService prayerRequestService;

    @PostMapping
    public ResponseEntity<PrayerRequestDto> createPrayerRequest(@Valid @RequestBody CreatePrayerRequestDto createDto,
                                                                Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        PrayerRequestDto prayerRequestDto = prayerRequestService.createPrayerRequest(createDto, userDetails.getId());
        return new ResponseEntity<>(prayerRequestDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrayerRequestDto> getPrayerRequestById(@PathVariable String id) {
        PrayerRequestDto prayerRequestDto = prayerRequestService.getPrayerRequestById(id);
        return ResponseEntity.ok(prayerRequestDto);
    }

    @GetMapping
    public ResponseEntity<List<PrayerRequestDto>> getAllActivePrayerRequests() {
        List<PrayerRequestDto> prayerRequests = prayerRequestService.getAllActivePrayerRequests();
        return ResponseEntity.ok(prayerRequests);
    }

    @GetMapping("/my")
    public ResponseEntity<List<PrayerRequestDto>> getMyPrayerRequests(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<PrayerRequestDto> prayerRequests = prayerRequestService.getPrayerRequestsByCreator(userDetails.getId());
        return ResponseEntity.ok(prayerRequests);
    }

    @GetMapping("/prayer-list/{prayerListId}")
    public ResponseEntity<List<PrayerRequestDto>> getPrayerRequestsByPrayerList(@PathVariable String prayerListId) {
        List<PrayerRequestDto> prayerRequests = prayerRequestService.getPrayerRequestsByPrayerList(prayerListId);
        return ResponseEntity.ok(prayerRequests);
    }

    @GetMapping("/church/{churchId}")
    public ResponseEntity<List<PrayerRequestDto>> getPrayerRequestsByChurch(@PathVariable String churchId) {
        List<PrayerRequestDto> prayerRequests = prayerRequestService.getPrayerRequestsByChurch(churchId);
        return ResponseEntity.ok(prayerRequests);
    }

    @GetMapping("/public")
    public ResponseEntity<List<PrayerRequestDto>> getPublicPrayerRequests() {
        List<PrayerRequestDto> prayerRequests = prayerRequestService.getPublicPrayerRequests();
        return ResponseEntity.ok(prayerRequests);
    }

    @GetMapping("/accessible")
    public ResponseEntity<List<PrayerRequestDto>> getAccessiblePrayerRequests(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<PrayerRequestDto> prayerRequests = prayerRequestService.getAccessiblePrayerRequests(userDetails.getId());
        return ResponseEntity.ok(prayerRequests);
    }

    @GetMapping("/urgent")
    public ResponseEntity<List<PrayerRequestDto>> getUrgentPrayerRequests() {
        List<PrayerRequestDto> prayerRequests = prayerRequestService.getUrgentPrayerRequests();
        return ResponseEntity.ok(prayerRequests);
    }

    @GetMapping("/answered")
    public ResponseEntity<List<PrayerRequestDto>> getAnsweredPrayerRequests(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate) {
        if (fromDate == null) {
            fromDate = LocalDateTime.now().minusMonths(3); // Default to last 3 months
        }
        List<PrayerRequestDto> prayerRequests = prayerRequestService.getAnsweredPrayerRequests(fromDate);
        return ResponseEntity.ok(prayerRequests);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PrayerRequestDto>> searchPrayerRequests(@RequestParam String q) {
        List<PrayerRequestDto> prayerRequests = prayerRequestService.searchPrayerRequests(q);
        return ResponseEntity.ok(prayerRequests);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrayerRequestDto> updatePrayerRequest(@PathVariable String id,
                                                                @Valid @RequestBody CreatePrayerRequestDto updateDto,
                                                                Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        PrayerRequestDto updatedPrayerRequest = prayerRequestService.updatePrayerRequest(id, updateDto, userDetails.getId());
        return ResponseEntity.ok(updatedPrayerRequest);
    }

    @PostMapping("/{id}/answered")
    public ResponseEntity<PrayerRequestDto> markAsAnswered(@PathVariable String id,
                                                           @RequestBody Map<String, String> request,
                                                           Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String answeredDescription = request.get("answeredDescription");
        PrayerRequestDto updatedPrayerRequest = prayerRequestService.markAsAnswered(id, answeredDescription, userDetails.getId());
        return ResponseEntity.ok(updatedPrayerRequest);
    }

    @PostMapping("/{id}/pray")
    public ResponseEntity<PrayerRequestDto> incrementPrayerCount(@PathVariable String id) {
        PrayerRequestDto updatedPrayerRequest = prayerRequestService.incrementPrayerCount(id);
        return ResponseEntity.ok(updatedPrayerRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrayerRequest(@PathVariable String id, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        prayerRequestService.deletePrayerRequest(id, userDetails.getId());
        return ResponseEntity.ok("Prayer request deleted successfully");
    }
}