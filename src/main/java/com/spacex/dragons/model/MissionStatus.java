package com.spacex.dragons.model;

import lombok.Getter;

@Getter
public enum MissionStatus {
    SCHEDULED("Scheduled"), PENDING("Pending"),
    IN_PROGRESS("In Progress"), ENDED("Ended");

    private final String description;

    MissionStatus(String description) {
        this.description = description;
    }

}
