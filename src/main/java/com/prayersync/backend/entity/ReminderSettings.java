package com.prayersync.backend.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;

@Embeddable
public class ReminderSettings {
    
    @Column(name = "reminder_enabled")
    private Boolean reminderEnabled = false;
    
    @Column(name = "reminder_frequency_days")
    private Integer reminderFrequencyDays = 7;
    
    @Column(name = "reminder_time")
    private String reminderTime = "09:00";

    public ReminderSettings() {}

    public ReminderSettings(Boolean reminderEnabled, Integer reminderFrequencyDays, String reminderTime) {
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