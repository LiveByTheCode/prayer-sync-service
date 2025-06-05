package com.prayersync.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sync_log")
public class SyncLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Column(name = "sync_type", nullable = false, length = 50)
    private String syncType;
    
    @Column(name = "sync_start", nullable = false)
    private LocalDateTime syncStart;
    
    @Column(name = "sync_end")
    private LocalDateTime syncEnd;
    
    @Column(name = "items_uploaded")
    private Integer itemsUploaded = 0;
    
    @Column(name = "items_downloaded")
    private Integer itemsDownloaded = 0;
    
    @Column(name = "conflicts_resolved")
    private Integer conflictsResolved = 0;
    
    @Column(name = "status", nullable = false, length = 50)
    private String status = "IN_PROGRESS";
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    public SyncLog() {}

    public SyncLog(String userId, String syncType) {
        this.userId = userId;
        this.syncType = syncType;
        this.syncStart = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public void markAsCompleted() {
        this.status = "COMPLETED";
        this.syncEnd = LocalDateTime.now();
    }

    public void markAsFailed(String errorMessage) {
        this.status = "FAILED";
        this.syncEnd = LocalDateTime.now();
        this.errorMessage = errorMessage;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public LocalDateTime getSyncStart() {
        return syncStart;
    }

    public void setSyncStart(LocalDateTime syncStart) {
        this.syncStart = syncStart;
    }

    public LocalDateTime getSyncEnd() {
        return syncEnd;
    }

    public void setSyncEnd(LocalDateTime syncEnd) {
        this.syncEnd = syncEnd;
    }

    public Integer getItemsUploaded() {
        return itemsUploaded;
    }

    public void setItemsUploaded(Integer itemsUploaded) {
        this.itemsUploaded = itemsUploaded;
    }

    public Integer getItemsDownloaded() {
        return itemsDownloaded;
    }

    public void setItemsDownloaded(Integer itemsDownloaded) {
        this.itemsDownloaded = itemsDownloaded;
    }

    public Integer getConflictsResolved() {
        return conflictsResolved;
    }

    public void setConflictsResolved(Integer conflictsResolved) {
        this.conflictsResolved = conflictsResolved;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}