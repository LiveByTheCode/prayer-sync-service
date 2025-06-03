package com.prayersync.backend.dto;

import com.prayersync.backend.enums.RequestCategory;
import com.prayersync.backend.enums.RequestPriority;
import com.prayersync.backend.enums.PrivacyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreatePrayerRequestDto {
    
    @NotBlank(message = "Prayer request title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;
    
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;
    
    @NotNull(message = "Category is required")
    private RequestCategory category = RequestCategory.OTHER;
    
    @NotNull(message = "Priority is required")
    private RequestPriority priority = RequestPriority.MEDIUM;
    
    @NotNull(message = "Privacy level is required")
    private PrivacyLevel privacyLevel = PrivacyLevel.PUBLIC;
    
    private Boolean isAnonymous = false;
    
    private String prayerListId;

    public CreatePrayerRequestDto() {}

    public CreatePrayerRequestDto(String title, String description, RequestCategory category,
                                 RequestPriority priority, PrivacyLevel privacyLevel,
                                 Boolean isAnonymous, String prayerListId) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.privacyLevel = privacyLevel;
        this.isAnonymous = isAnonymous;
        this.prayerListId = prayerListId;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RequestCategory getCategory() {
        return category;
    }

    public void setCategory(RequestCategory category) {
        this.category = category;
    }

    public RequestPriority getPriority() {
        return priority;
    }

    public void setPriority(RequestPriority priority) {
        this.priority = priority;
    }

    public PrivacyLevel getPrivacyLevel() {
        return privacyLevel;
    }

    public void setPrivacyLevel(PrivacyLevel privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getPrayerListId() {
        return prayerListId;
    }

    public void setPrayerListId(String prayerListId) {
        this.prayerListId = prayerListId;
    }
}