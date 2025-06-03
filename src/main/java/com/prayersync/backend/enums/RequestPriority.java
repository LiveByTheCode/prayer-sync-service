package com.prayersync.backend.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum RequestPriority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String displayName;

    RequestPriority(String displayName) {
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
    public static RequestPriority fromJsonValue(String value) {
        if (value == null) {
            return null;
        }
        
        switch(value.toLowerCase()) {
            case "low":
                return LOW;
            case "medium":
                return MEDIUM;
            case "high":
                return HIGH;
            default:
                return MEDIUM;
        }
    }
}