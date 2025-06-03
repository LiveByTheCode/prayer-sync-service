package com.prayersync.backend.dto;

import java.time.LocalDateTime;

public class UserDto {
    
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String bio;
    private String profileImageUrl;
    private Boolean isActive;
    private Boolean isChurchAdmin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String churchId;
    private String churchName;
    private ReminderSettingsDto reminderSettings;

    public UserDto() {}

    public UserDto(String id, String email, String firstName, String lastName, String phone,
                   String bio, String profileImageUrl, Boolean isActive, Boolean isChurchAdmin,
                   LocalDateTime createdAt, LocalDateTime updatedAt, String churchId,
                   String churchName, ReminderSettingsDto reminderSettings) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
        this.isActive = isActive;
        this.isChurchAdmin = isChurchAdmin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.churchId = churchId;
        this.churchName = churchName;
        this.reminderSettings = reminderSettings;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsChurchAdmin() {
        return isChurchAdmin;
    }

    public void setIsChurchAdmin(Boolean isChurchAdmin) {
        this.isChurchAdmin = isChurchAdmin;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getChurchId() {
        return churchId;
    }

    public void setChurchId(String churchId) {
        this.churchId = churchId;
    }

    public String getChurchName() {
        return churchName;
    }

    public void setChurchName(String churchName) {
        this.churchName = churchName;
    }

    public ReminderSettingsDto getReminderSettings() {
        return reminderSettings;
    }

    public void setReminderSettings(ReminderSettingsDto reminderSettings) {
        this.reminderSettings = reminderSettings;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    // Add displayName for Flutter app compatibility
    public String getDisplayName() {
        if (lastName != null && !lastName.trim().isEmpty()) {
            return firstName + " " + lastName;
        }
        return firstName;
    }
}