package com.prayersync.backend.controller;

import com.prayersync.backend.dto.ChurchDto;
import com.prayersync.backend.dto.CreateChurchDto;
import com.prayersync.backend.service.ChurchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/churches")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChurchController {

    @Autowired
    private ChurchService churchService;

    @PostMapping
    @PreAuthorize("hasRole('CHURCH_ADMIN')")
    public ResponseEntity<ChurchDto> createChurch(@Valid @RequestBody CreateChurchDto createChurchDto) {
        ChurchDto churchDto = churchService.createChurch(createChurchDto);
        return new ResponseEntity<>(churchDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChurchDto> getChurchById(@PathVariable Long id) {
        ChurchDto churchDto = churchService.getChurchById(id);
        return ResponseEntity.ok(churchDto);
    }

    @GetMapping
    public ResponseEntity<List<ChurchDto>> getAllChurches() {
        List<ChurchDto> churches = churchService.getAllChurches();
        return ResponseEntity.ok(churches);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ChurchDto>> searchChurches(@RequestParam String q) {
        List<ChurchDto> churches = churchService.searchChurches(q);
        return ResponseEntity.ok(churches);
    }

    @GetMapping("/near")
    public ResponseEntity<List<ChurchDto>> getChurchesNearLocation(@RequestParam String location) {
        List<ChurchDto> churches = churchService.getChurchesNearLocation(location);
        return ResponseEntity.ok(churches);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CHURCH_ADMIN')")
    public ResponseEntity<ChurchDto> updateChurch(@PathVariable Long id, 
                                                  @Valid @RequestBody CreateChurchDto updateDto) {
        ChurchDto updatedChurch = churchService.updateChurch(id, updateDto);
        return ResponseEntity.ok(updatedChurch);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CHURCH_ADMIN')")
    public ResponseEntity<?> deleteChurch(@PathVariable Long id) {
        churchService.deleteChurch(id);
        return ResponseEntity.ok("Church deleted successfully");
    }
}