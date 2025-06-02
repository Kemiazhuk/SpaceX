package com.spacex.dragons.service;

import com.spacex.dragons.model.Mission;
import com.spacex.dragons.model.MissionStatus;
import com.spacex.dragons.model.MissionSummary;
import com.spacex.dragons.model.Rocket;
import com.spacex.dragons.model.RocketInfo;
import com.spacex.dragons.model.RocketStatus;
import com.spacex.dragons.repository.MissionRepository;
import com.spacex.dragons.repository.RocketRepository;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.spacex.dragons.utils.MissionStatusUtils.calculateStatus;
import static com.spacex.dragons.utils.ValidationUtils.requireFound;
import static com.spacex.dragons.utils.ValidationUtils.requireNonEmpty;
import static com.spacex.dragons.utils.ValidationUtils.requireNonNull;
import static com.spacex.dragons.utils.ValidationUtils.requireState;

@AllArgsConstructor
public class SpaceXRocketService {

    private final RocketRepository rocketRepository;
    private final MissionRepository missionRepository;

    public void addRocket(String rocketId) {
        requireNonEmpty(rocketId, "Rocket name");
        rocketRepository.addRocket(rocketId);
    }

    public void addMission(String missionId) {
        requireNonEmpty(missionId, "Mission name");
        missionRepository.addMission(missionId);
    }

    public void assignRocketToMission(String rocketId, String missionId) {
        requireNonEmpty(rocketId, "Rocket name");
        requireNonEmpty(missionId, "Mission name");

        Rocket rocket = rocketRepository.getRocketById(rocketId);
        Mission mission = missionRepository.getMissionById(missionId);

        requireFound(rocket, "Rocket not found");
        requireFound(mission, "Mission not found");
        requireState(rocket.getMission() == null, "Rocket already assigned");
        requireState(mission.getMissionStatus() != MissionStatus.ENDED, "Mission ended");

        rocketRepository.assignRocketToMission(rocket, mission);
        missionRepository.assignRocketToMission(rocket, mission);
    }

    public void changeRocketStatus(String rocketId, RocketStatus status) {
        requireNonEmpty(rocketId, "Rocket name");
        requireNonNull(status, "Rocket status");

        Rocket rocket = rocketRepository.getRocketById(rocketId);
        requireFound(rocket, "Rocket not found");

        Mission mission = rocket.getMission();
        if (mission != null) {
            rocket.setRocketStatus(status);
            mission.setMissionStatus(calculateStatus(mission));
        }
    }

    public void changeMissionStatus(String missionId, MissionStatus status) {
        requireNonEmpty(missionId, "Mission name");
        requireNonNull(status, "Mission status");

        Mission mission = missionRepository.getMissionById(missionId);
        requireFound(mission, "Mission not found");

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
                mission.getMissionStatus(),
                mission.getRockets().size(),
                rocketInfos
        );
    }

    private RocketInfo buildRocketInfo(Rocket rocket) {
        return new RocketInfo(rocket.getRocketId(), rocket.getRocketStatus());
    }
}