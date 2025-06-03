package com.prayersync.backend.enums;

public enum RequestCategory {
    PERSONAL("Personal"),
    FAMILY("Family"),
    HEALTH("Health"),
    WORK("Work"),
    MINISTRY("Ministry"),
    COMMUNITY("Community"),
    SALVATION("Salvation"),
    PRAISE("Praise"),
    OTHER("Other");

    private final String displayName;

    RequestCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}