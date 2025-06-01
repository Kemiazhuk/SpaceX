package com.spacex.dragons.model;

import lombok.Getter;

@Getter
public enum RocketStatus {
    ON_GROUND("On ground"), IN_SPACE("In space"), IN_REPAIR("In repair");

    private final String description;

    RocketStatus(String description) {
        this.description = description;
    }

}
