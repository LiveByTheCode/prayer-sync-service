package com.prayersync.backend.enums;

public enum PrivacyLevel {
    PUBLIC("Public"),
    CHURCH_ONLY("Church Only"),
    PRIVATE("Private");

    private final String displayName;

    PrivacyLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}