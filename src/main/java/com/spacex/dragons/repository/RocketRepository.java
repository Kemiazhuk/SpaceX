package com.spacex.dragons.repository;

import com.spacex.dragons.model.Rocket;

import java.util.HashMap;
import java.util.Map;

public class RocketRepository {

    private final Map<String, Rocket> rockets = new HashMap<>();

    public void addRocket(Rocket rocket) {
        rockets.put(rocket.getRocketId(), rocket);
    }

    public Rocket getRocketById(String rocketId) {
        return rockets.get(rocketId);
    }
}
