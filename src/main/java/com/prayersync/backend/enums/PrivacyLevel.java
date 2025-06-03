package com.prayersync.backend.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum PrivacyLevel {
    PUBLIC("Public"),
    CHURCH("Church"),
    PRIVATE("Private");

    private final String displayName;

    PrivacyLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    @JsonValue
    public String toJsonValue() {
        return this.name().toLowerCase();
    }
    
    @JsonCreator
    public static PrivacyLevel fromJsonValue(String value) {
        if (value == null) {
            return null;
        }
        
        switch(value.toLowerCase()) {
            case "public":
                return PUBLIC;
            case "church":
                return CHURCH;
            case "private":
                return PRIVATE;
            default:
                return PRIVATE;
        }
    }
}