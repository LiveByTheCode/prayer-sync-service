package com.prayersync.backend.dto;

import java.time.LocalDateTime;

public class ConflictDto {
    
    public enum ConflictType {
        PRAYER_REQUEST, PRAYER_LIST
    }
    
    public enum ConflictResolution {
        CLIENT_WINS, SERVER_WINS, MANUAL_REQUIRED
    }
    
    private String entityId;
    private ConflictType type;
    private ConflictResolution resolution;
    private String clientVersion;
    private String serverVersion;
    private LocalDateTime clientModified;
    private LocalDateTime serverModified;
    private String message;

    public ConflictDto() {}

    public ConflictDto(String entityId, ConflictType type, ConflictResolution resolution,
                      String clientVersion, String serverVersion,
                      LocalDateTime clientModified, LocalDateTime serverModified,
                      String message) {
        this.entityId = entityId;
        this.type = type;
        this.resolution = resolution;
        this.clientVersion = clientVersion;
        this.serverVersion = serverVersion;
        this.clientModified = clientModified;
        this.serverModified = serverModified;
        this.message = message;
    }

    // Getters and Setters
    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public ConflictType getType() {
        return type;
    }

    public void setType(ConflictType type) {
        this.type = type;
    }

    public ConflictResolution getResolution() {
        return resolution;
    }

    public void setResolution(ConflictResolution resolution) {
        this.resolution = resolution;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public LocalDateTime getClientModified() {
        return clientModified;
    }

    public void setClientModified(LocalDateTime clientModified) {
        this.clientModified = clientModified;
    }

    public LocalDateTime getServerModified() {
        return serverModified;
    }

    public void setServerModified(LocalDateTime serverModified) {
        this.serverModified = serverModified;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}