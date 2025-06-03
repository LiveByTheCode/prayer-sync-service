package com.prayersync.backend.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum RequestStatus {
    ACTIVE("Active"),
    ANSWERED("Answered"),
    NO_LONGER_NEEDED("No Longer Needed"),
    ONGOING("Ongoing");

    private final String displayName;

    RequestStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    @JsonValue
    public String toJsonValue() {
        switch(this) {
            case NO_LONGER_NEEDED:
                return "noLongerNeeded";
            default:
                return this.name().toLowerCase();
        }
    }
    
    @JsonCreator
    public static RequestStatus fromJsonValue(String value) {
        if (value == null) {
            return null;
        }
        
        switch(value.toLowerCase()) {
            case "active":
                return ACTIVE;
            case "answered":
                return ANSWERED;
            case "nolongerneeded":
                return NO_LONGER_NEEDED;
            case "ongoing":
                return ONGOING;
            default:
                return ACTIVE;
        }
    }
}