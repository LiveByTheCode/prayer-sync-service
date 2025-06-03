package com.prayersync.backend.dto;

import com.prayersync.backend.enums.PrivacyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreatePrayerListDto {
    
    @NotBlank(message = "Prayer list name is required")
    @Size(max = 255, message = "Prayer list name cannot exceed 255 characters")
    private String name;
    
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;
    
    @NotNull(message = "Privacy level is required")
    private PrivacyLevel privacyLevel = PrivacyLevel.PUBLIC;
    
    private Long churchId;

    public CreatePrayerListDto() {}

    public CreatePrayerListDto(String name, String description, PrivacyLevel privacyLevel, Long churchId) {
        this.name = name;
        this.description = description;
        this.privacyLevel = privacyLevel;
        this.churchId = churchId;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PrivacyLevel getPrivacyLevel() {
        return privacyLevel;
    }

    public void setPrivacyLevel(PrivacyLevel privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

    public Long getChurchId() {
        return churchId;
    }

    public void setChurchId(Long churchId) {
        this.churchId = churchId;
    }
}