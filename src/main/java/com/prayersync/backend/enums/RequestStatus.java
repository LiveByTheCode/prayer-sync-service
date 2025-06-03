package com.prayersync.backend.enums;

public enum RequestStatus {
    ACTIVE("Active"),
    ANSWERED("Answered"),
    CLOSED("Closed"),
    PAUSED("Paused");

    private final String displayName;

    RequestStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}