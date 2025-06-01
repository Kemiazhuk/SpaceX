package com.spacex.dragons.repository;

import com.spacex.dragons.model.Mission;
import com.spacex.dragons.model.Rocket;
import com.spacex.dragons.model.RocketStatus;

import java.util.HashMap;
import java.util.Map;

public class RocketRepository implements IRocketRepository {

    private final Map<String, Rocket> rockets = new HashMap<>();

    public void addRocket(String rocketId) {
        Rocket rocket = new Rocket(rocketId);
        rockets.put(rocket.getRocketId(), rocket);
    }

    public Rocket getRocketById(String rocketId) {
        return rockets.get(rocketId);
    }

    public void assignRocketToMission(Rocket rocket, Mission mission){
        rocket.setMission(mission);
        rocket.setRocketStatus(RocketStatus.IN_SPACE);
    }
}
