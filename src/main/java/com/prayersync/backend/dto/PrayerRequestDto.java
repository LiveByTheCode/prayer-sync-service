package com.prayersync.backend.dto;

import com.prayersync.backend.enums.RequestCategory;
import com.prayersync.backend.enums.RequestPriority;
import com.prayersync.backend.enums.RequestStatus;
import com.prayersync.backend.enums.PrivacyLevel;
import java.time.LocalDateTime;

public class PrayerRequestDto {
    
    private String id;
    private String title;
    private String description;
    private RequestCategory category;
    private RequestPriority priority;
    private RequestStatus status;
    private PrivacyLevel privacyLevel;
    private Integer prayerCount;
    private Boolean isAnonymous;
    private LocalDateTime answeredAt;
    private String answeredDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String creatorId;
    private String creatorName;
    private String prayerListId;
    private String prayerListName;

    public PrayerRequestDto() {}

    public PrayerRequestDto(String id, String title, String description, RequestCategory category,
                           RequestPriority priority, RequestStatus status, PrivacyLevel privacyLevel,
                           Integer prayerCount, Boolean isAnonymous, LocalDateTime answeredAt,
                           String answeredDescription, LocalDateTime createdAt, LocalDateTime updatedAt,
                           String creatorId, String creatorName, String prayerListId, String prayerListName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.privacyLevel = privacyLevel;
        this.prayerCount = prayerCount;
        this.isAnonymous = isAnonymous;
        this.answeredAt = answeredAt;
        this.answeredDescription = answeredDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.prayerListId = prayerListId;
        this.prayerListName = prayerListName;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RequestCategory getCategory() {
        return category;
    }

    public void setCategory(RequestCategory category) {
        this.category = category;
    }

    public RequestPriority getPriority() {
        return priority;
    }

    public void setPriority(RequestPriority priority) {
        this.priority = priority;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public PrivacyLevel getPrivacyLevel() {
        return privacyLevel;
    }

    public void setPrivacyLevel(PrivacyLevel privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

    public Integer getPrayerCount() {
        return prayerCount;
    }

    public void setPrayerCount(Integer prayerCount) {
        this.prayerCount = prayerCount;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public LocalDateTime getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(LocalDateTime answeredAt) {
        this.answeredAt = answeredAt;
    }

    public String getAnsweredDescription() {
        return answeredDescription;
    }

    public void setAnsweredDescription(String answeredDescription) {
        this.answeredDescription = answeredDescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getPrayerListId() {
        return prayerListId;
    }

    public void setPrayerListId(String prayerListId) {
        this.prayerListId = prayerListId;
    }

    public String getPrayerListName() {
        return prayerListName;
    }

    public void setPrayerListName(String prayerListName) {
        this.prayerListName = prayerListName;
    }
}