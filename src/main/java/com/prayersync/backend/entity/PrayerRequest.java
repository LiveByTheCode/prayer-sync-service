package com.prayersync.backend.entity;

import com.prayersync.backend.enums.RequestCategory;
import com.prayersync.backend.enums.RequestPriority;
import com.prayersync.backend.enums.RequestStatus;
import com.prayersync.backend.enums.PrivacyLevel;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "prayer_requests")
public class PrayerRequest {
    
    @Id
    private String id;
    
    @Column(name = "title", nullable = false, length = 255)
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private RequestCategory category = RequestCategory.OTHER;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private RequestPriority priority = RequestPriority.MEDIUM;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status = RequestStatus.ACTIVE;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "privacy_level", nullable = false)
    private PrivacyLevel privacyLevel = PrivacyLevel.PUBLIC;
    
    @Column(name = "prayer_count", nullable = false)
    private Integer prayerCount = 0;
    
    @Column(name = "is_anonymous", nullable = false)
    private Boolean isAnonymous = false;
    
    @Column(name = "answered_at")
    private LocalDateTime answeredAt;
    
    @Column(name = "answered_description", columnDefinition = "TEXT")
    private String answeredDescription;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Sync metadata
    @Column(name = "sync_id")
    private String syncId;
    
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prayer_list_id")
    private PrayerList prayerList;

    public PrayerRequest() {}

    public PrayerRequest(String title, String description, RequestCategory category, 
                        RequestPriority priority, PrivacyLevel privacyLevel, 
                        Boolean isAnonymous, User creator, PrayerList prayerList) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.privacyLevel = privacyLevel;
        this.isAnonymous = isAnonymous;
        this.creator = creator;
        this.prayerList = prayerList;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsAnswered(String description) {
        this.status = RequestStatus.ANSWERED;
        this.answeredAt = LocalDateTime.now();
        this.answeredDescription = description;
    }

    public void incrementPrayerCount() {
        this.prayerCount++;
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public PrayerList getPrayerList() {
        return prayerList;
    }

    public void setPrayerList(PrayerList prayerList) {
        this.prayerList = prayerList;
    }
    
    // Sync metadata getters and setters
    public String getSyncId() {
        return syncId;
    }
    
    public void setSyncId(String syncId) {
        this.syncId = syncId;
    }
    
    public Boolean getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
    
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
    
    public void markAsDeleted() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }
    
    // Convenience methods for creator ID
    public String getCreatorId() {
        return creator != null ? creator.getId() : null;
    }
    
    public void setCreatorId(String creatorId) {
        if (creatorId != null && !creatorId.isEmpty()) {
            // Create a minimal User object for the foreign key
            User user = new User();
            user.setId(creatorId);
            this.creator = user;
        }
    }
}