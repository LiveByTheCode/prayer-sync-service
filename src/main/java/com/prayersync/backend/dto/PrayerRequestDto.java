package com.prayersync.backend.dto;

import com.prayersync.backend.enums.RequestCategory;
import com.prayersync.backend.enums.RequestPriority;
import com.prayersync.backend.enums.RequestStatus;
import com.prayersync.backend.enums.PrivacyLevel;
import java.time.LocalDateTime;

public class PrayerRequestDto {
    
    private Long id;
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
    private Long creatorId;
    private String creatorName;
    private Long prayerListId;
    private String prayerListName;

    public PrayerRequestDto() {}

    public PrayerRequestDto(Long id, String title, String description, RequestCategory category,
                           RequestPriority priority, RequestStatus status, PrivacyLevel privacyLevel,
                           Integer prayerCount, Boolean isAnonymous, LocalDateTime answeredAt,
                           String answeredDescription, LocalDateTime createdAt, LocalDateTime updatedAt,
                           Long creatorId, String creatorName, Long prayerListId, String prayerListName) {
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getPrayerListId() {
        return prayerListId;
    }

    public void setPrayerListId(Long prayerListId) {
        this.prayerListId = prayerListId;
    }

    public String getPrayerListName() {
        return prayerListName;
    }

    public void setPrayerListName(String prayerListName) {
        this.prayerListName = prayerListName;
    }
}