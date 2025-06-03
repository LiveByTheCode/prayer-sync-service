package com.prayersync.backend.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum RequestCategory {
    HEALTH("Health"),
    FAMILY("Family"),
    WORK("Work"),
    SPIRITUAL_GROWTH("Spiritual Growth"),
    RELATIONSHIPS("Relationships"),
    FINANCES("Finances"),
    MINISTRY("Ministry"),
    OTHER("Other");

    private final String displayName;

    RequestCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    @JsonValue
    public String toJsonValue() {
        switch(this) {
            case SPIRITUAL_GROWTH:
                return "spiritualGrowth";
            default:
                return this.name().toLowerCase();
        }
    }
    
    @JsonCreator
    public static RequestCategory fromJsonValue(String value) {
        if (value == null) {
            return null;
        }
        
        switch(value.toLowerCase()) {
            case "health":
                return HEALTH;
            case "family":
                return FAMILY;
            case "work":
                return WORK;
            case "spiritualgrowth":
                return SPIRITUAL_GROWTH;
            case "relationships":
                return RELATIONSHIPS;
            case "finances":
                return FINANCES;
            case "ministry":
                return MINISTRY;
            case "other":
            default:
                return OTHER;
        }
    }
}