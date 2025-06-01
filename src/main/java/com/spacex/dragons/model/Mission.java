package com.spacex.dragons.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Mission {
    private MissionStatus missionStatus;
    private String missionId;
    private List<Rocket> rockets;

    public Mission(String missionId) {
        this.missionId = missionId;
        this.missionStatus = MissionStatus.SCHEDULED;
        this.rockets = new ArrayList<>();
    }
}
