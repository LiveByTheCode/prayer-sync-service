package com.prayersync.backend.controller;

import com.prayersync.backend.dto.CreatePrayerListDto;
import com.prayersync.backend.dto.PrayerListDto;
import com.prayersync.backend.security.CustomUserDetails;
import com.prayersync.backend.service.PrayerListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prayer-lists")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PrayerListController {

    @Autowired
    private PrayerListService prayerListService;

    @PostMapping
    public ResponseEntity<PrayerListDto> createPrayerList(@Valid @RequestBody CreatePrayerListDto createDto,
                                                          Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        PrayerListDto prayerListDto = prayerListService.createPrayerList(createDto, userDetails.getId());
        return new ResponseEntity<>(prayerListDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrayerListDto> getPrayerListById(@PathVariable Long id) {
        PrayerListDto prayerListDto = prayerListService.getPrayerListById(id);
        return ResponseEntity.ok(prayerListDto);
    }

    @GetMapping
    public ResponseEntity<List<PrayerListDto>> getAllPrayerLists() {
        List<PrayerListDto> prayerLists = prayerListService.getAllPrayerLists();
        return ResponseEntity.ok(prayerLists);
    }

    @GetMapping("/my")
    public ResponseEntity<List<PrayerListDto>> getMyPrayerLists(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<PrayerListDto> prayerLists = prayerListService.getPrayerListsByCreator(userDetails.getId());
        return ResponseEntity.ok(prayerLists);
    }

    @GetMapping("/church/{churchId}")
    public ResponseEntity<List<PrayerListDto>> getPrayerListsByChurch(@PathVariable Long churchId) {
        List<PrayerListDto> prayerLists = prayerListService.getPrayerListsByChurch(churchId);
        return ResponseEntity.ok(prayerLists);
    }

    @GetMapping("/public")
    public ResponseEntity<List<PrayerListDto>> getPublicPrayerLists() {
        List<PrayerListDto> prayerLists = prayerListService.getPublicPrayerLists();
        return ResponseEntity.ok(prayerLists);
    }

    @GetMapping("/accessible")
    public ResponseEntity<List<PrayerListDto>> getAccessiblePrayerLists(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<PrayerListDto> prayerLists = prayerListService.getAccessiblePrayerLists(userDetails.getId());
        return ResponseEntity.ok(prayerLists);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PrayerListDto>> searchPrayerLists(@RequestParam String q) {
        List<PrayerListDto> prayerLists = prayerListService.searchPrayerLists(q);
        return ResponseEntity.ok(prayerLists);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrayerListDto> updatePrayerList(@PathVariable Long id,
                                                          @Valid @RequestBody CreatePrayerListDto updateDto,
                                                          Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        PrayerListDto updatedPrayerList = prayerListService.updatePrayerList(id, updateDto, userDetails.getId());
        return ResponseEntity.ok(updatedPrayerList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrayerList(@PathVariable Long id, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        prayerListService.deletePrayerList(id, userDetails.getId());
        return ResponseEntity.ok("Prayer list deleted successfully");
    }
}