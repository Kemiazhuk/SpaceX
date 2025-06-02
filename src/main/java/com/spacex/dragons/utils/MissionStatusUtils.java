package com.spacex.dragons.utils;

import com.spacex.dragons.model.Mission;
import com.spacex.dragons.model.MissionStatus;
import com.spacex.dragons.model.RocketStatus;

import java.util.Objects;

public class MissionStatusUtils {

    public static MissionStatus calculateStatus(Mission mission) {
        if (mission.getMissionStatus() == MissionStatus.ENDED) {
            return MissionStatus.ENDED;
        }
        if (mission.getRockets().isEmpty()) {
            return MissionStatus.SCHEDULED;
        }
        boolean repairRocket = mission.getRockets().stream()
                .filter(Objects::nonNull)
                .anyMatch(rocket -> rocket.getRocketStatus() == RocketStatus.IN_REPAIR);
        return repairRocket ? MissionStatus.PENDING : MissionStatus.IN_PROGRESS;
    }
}
