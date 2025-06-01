package com.spacex.dragons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MissionSummary {
    private String missionId;
    private MissionStatus status;
    private int rocketAmount;
    private List<RocketInfo> rocketInfo;
}
