package com.spacex.dragons.repository;

import com.spacex.dragons.model.Mission;
import com.spacex.dragons.model.Rocket;

import java.util.Collection;

public interface IMissionRepository {

    void addMission(String missionId);

    Mission getMissionById(String missionId);

    Collection<Mission> getAllMissions();

    void assignRocketToMission(Rocket rocket, Mission mission);

}
