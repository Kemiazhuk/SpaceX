package com.spacex.dragons.repository;

import com.spacex.dragons.model.Mission;
import com.spacex.dragons.model.Rocket;

public interface IRocketRepository {
    void addRocket(String rocketId);

    Rocket getRocketById(String rocketId);

    void assignRocketToMission(Rocket rocket, Mission mission);
    }
