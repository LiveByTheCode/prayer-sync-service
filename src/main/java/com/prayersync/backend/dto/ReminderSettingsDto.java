package com.prayersync.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public class ReminderSettingsDto {
    
    private Boolean reminderEnabled = false;
    
    @Min(value = 1, message = "Reminder frequency must be at least 1 day")
    @Max(value = 365, message = "Reminder frequency cannot exceed 365 days")
    private Integer reminderFrequencyDays = 7;
    
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", 
             message = "Reminder time must be in HH:MM format")
    private String reminderTime = "09:00";

    public ReminderSettingsDto() {}

    public ReminderSettingsDto(Boolean reminderEnabled, Integer reminderFrequencyDays, String reminderTime) {
        this.reminderEnabled = reminderEnabled;
        this.reminderFrequencyDays = reminderFrequencyDays;
        this.reminderTime = reminderTime;
    }

    public Boolean getReminderEnabled() {
        return reminderEnabled;
    }

    public void setReminderEnabled(Boolean reminderEnabled) {
        this.reminderEnabled = reminderEnabled;
    }

    public Integer getReminderFrequencyDays() {
        return reminderFrequencyDays;
    }

    public void setReminderFrequencyDays(Integer reminderFrequencyDays) {
        this.reminderFrequencyDays = reminderFrequencyDays;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }
}