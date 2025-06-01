package com.spacex.dragons.service;

import com.spacex.dragons.model.Mission;
import com.spacex.dragons.model.MissionStatus;
import com.spacex.dragons.model.MissionSummary;
import com.spacex.dragons.model.Rocket;
import com.spacex.dragons.model.RocketInfo;
import com.spacex.dragons.model.RocketStatus;
import com.spacex.dragons.repository.MissionRepository;
import com.spacex.dragons.repository.RocketRepository;
import com.spacex.dragons.strategy.MissionStatusStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SpaceXRocketService {
    private final MissionStatusStrategy missionStatusStrategy = new MissionStatusStrategy();
    private final RocketRepository rocketRepository = new RocketRepository();
    private final MissionRepository missionRepository = new MissionRepository();

    public Rocket addRocket(String rocketId) {
        Rocket rocket = new Rocket(rocketId);
        rocketRepository.addRocket(rocket);
        return rocket;
    }

    public Mission addMission(String missionId) {
        Mission mission = new Mission(missionId);
        missionRepository.addMission(mission);
        return mission;
    }

    public void assignRocketToMission(String rocketId, String missionId) {
        Rocket rocket = rocketRepository.getRocketById(rocketId);
        Mission mission = missionRepository.getMissionById(missionId);
        if (rocket == null || mission == null) {
            throw new IllegalArgumentException("Not found");
        }
        if (rocket.getMission() != null) {
            throw new IllegalStateException("Rocket already assigned");
        }
        if (mission.getMissionStatus() == MissionStatus.ENDED) {
            throw new IllegalStateException("Mission ended");
        }
        rocket.setMission(mission);
        rocket.setRocketStatus(RocketStatus.IN_SPACE);
        mission.getRockets().add(rocket);
        mission.setMissionStatus(missionStatusStrategy.calculateStatus(mission));
    }

    public void changeRocketStatus(String rocketId, RocketStatus status) {
        Rocket rocket = rocketRepository.getRocketById(rocketId);
        if (rocket == null) {
            throw new IllegalArgumentException("Rocket not found");
        }
        Mission mission = rocket.getMission();
        if (mission != null) {
            rocket.setRocketStatus(status);
            mission.setMissionStatus(missionStatusStrategy.calculateStatus(mission));
        }
    }

    public void changeMissionStatus(String missionId, MissionStatus status) {
        Mission mission = missionRepository.getMissionById(missionId);
        if (mission == null) {
            throw new IllegalArgumentException("Mission not found");
        }
        if (status == MissionStatus.ENDED) {
            for (Rocket rocket : mission.getRockets()) {
                rocket.setRocketStatus(null);
            }
            mission.getRockets().clear();
        }
        mission.setMissionStatus(status);
    }

    public List<MissionSummary> getMissionsSummary() {
        return missionRepository.getAllMissions()
                .stream()
                .sorted(Comparator
                        .comparingInt((Mission mission) -> mission.getRockets().size())
                        .reversed()
                        .thenComparing(Mission::getMissionId, Comparator.reverseOrder()))
                .map(this::buildMissionSummary)
                .collect(Collectors.toList());
    }

    private MissionSummary buildMissionSummary(Mission mission) {
        List<RocketInfo> rocketInfos = mission.getRockets()
                .stream()
                .map(this::buildRocketInfo)
                .toList();
        return new MissionSummary(
                mission.getMissionId(),
                mission.getMissionStatus().getDescription(),
                mission.getRockets().size(),
                rocketInfos
        );
    }

    private RocketInfo buildRocketInfo(Rocket rocket) {
        return new RocketInfo(rocket.getRocketId(), rocket.getRocketStatus().getDescription());
    }
}