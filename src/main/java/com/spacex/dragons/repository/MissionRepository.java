package com.spacex.dragons.repository;

import com.spacex.dragons.model.Mission;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MissionRepository {

    private final Map<String, Mission> missions = new HashMap<>();

    public void addMission(Mission mission) {
        missions.put(mission.getMissionId(), mission);
    }

    public Mission getMissionById(String missionId) {
        return missions.get(missionId);
    }

    public Collection<Mission> getAllMissions() {
        return missions.values();
    }
}

