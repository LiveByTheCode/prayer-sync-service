package com.prayersync.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SyncResponseDto {
    
    private LocalDateTime serverTime;
    private List<PrayerRequestDto> updatedPrayerRequests;
    private List<PrayerListDto> updatedPrayerLists;
    private List<String> deletedPrayerRequestIds;
    private List<String> deletedPrayerListIds;
    private List<ConflictDto> conflicts;

    public SyncResponseDto() {}

    public SyncResponseDto(LocalDateTime serverTime,
                          List<PrayerRequestDto> updatedPrayerRequests,
                          List<PrayerListDto> updatedPrayerLists,
                          List<String> deletedPrayerRequestIds,
                          List<String> deletedPrayerListIds,
                          List<ConflictDto> conflicts) {
        this.serverTime = serverTime;
        this.updatedPrayerRequests = updatedPrayerRequests;
        this.updatedPrayerLists = updatedPrayerLists;
        this.deletedPrayerRequestIds = deletedPrayerRequestIds;
        this.deletedPrayerListIds = deletedPrayerListIds;
        this.conflicts = conflicts;
    }

    // Getters and Setters
    public LocalDateTime getServerTime() {
        return serverTime;
    }

    public void setServerTime(LocalDateTime serverTime) {
        this.serverTime = serverTime;
    }

    public List<PrayerRequestDto> getUpdatedPrayerRequests() {
        return updatedPrayerRequests;
    }

    public void setUpdatedPrayerRequests(List<PrayerRequestDto> updatedPrayerRequests) {
        this.updatedPrayerRequests = updatedPrayerRequests;
    }

    public List<PrayerListDto> getUpdatedPrayerLists() {
        return updatedPrayerLists;
    }

    public void setUpdatedPrayerLists(List<PrayerListDto> updatedPrayerLists) {
        this.updatedPrayerLists = updatedPrayerLists;
    }

    public List<String> getDeletedPrayerRequestIds() {
        return deletedPrayerRequestIds;
    }

    public void setDeletedPrayerRequestIds(List<String> deletedPrayerRequestIds) {
        this.deletedPrayerRequestIds = deletedPrayerRequestIds;
    }

    public List<String> getDeletedPrayerListIds() {
        return deletedPrayerListIds;
    }

    public void setDeletedPrayerListIds(List<String> deletedPrayerListIds) {
        this.deletedPrayerListIds = deletedPrayerListIds;
    }

    public List<ConflictDto> getConflicts() {
        return conflicts;
    }

    public void setConflicts(List<ConflictDto> conflicts) {
        this.conflicts = conflicts;
    }
}