package com.prayersync.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SyncRequestDto {
    
    private LocalDateTime lastSyncTime;
    private List<PrayerRequestDto> updatedPrayerRequests;
    private List<PrayerListDto> updatedPrayerLists;
    private List<String> deletedPrayerRequestIds;
    private List<String> deletedPrayerListIds;

    public SyncRequestDto() {}

    public SyncRequestDto(LocalDateTime lastSyncTime, 
                         List<PrayerRequestDto> updatedPrayerRequests,
                         List<PrayerListDto> updatedPrayerLists,
                         List<String> deletedPrayerRequestIds,
                         List<String> deletedPrayerListIds) {
        this.lastSyncTime = lastSyncTime;
        this.updatedPrayerRequests = updatedPrayerRequests;
        this.updatedPrayerLists = updatedPrayerLists;
        this.deletedPrayerRequestIds = deletedPrayerRequestIds;
        this.deletedPrayerListIds = deletedPrayerListIds;
    }

    // Getters and Setters
    public LocalDateTime getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(LocalDateTime lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
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
}