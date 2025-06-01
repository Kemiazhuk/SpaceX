package com.spacex.dragons.repository;

import com.spacex.dragons.model.Mission;
import com.spacex.dragons.model.Rocket;
import com.spacex.dragons.utils.MissionStatusUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MissionRepository implements IMissionRepository {

    private final Map<String, Mission> missions = new HashMap<>();

    public void addMission(String missionId) {
        Mission mission = new Mission(missionId);
        missions.put(mission.getMissionId(), mission);
    }

    public Mission getMissionById(String missionId) {
        return missions.get(missionId);
    }

    public Collection<Mission> getAllMissions() {
        return missions.values();
    }

    public void assignRocketToMission(Rocket rocket, Mission mission) {
        mission.getRockets().add(rocket);
        mission.setMissionStatus(MissionStatusUtil.calculateStatus(mission));
    }
}

