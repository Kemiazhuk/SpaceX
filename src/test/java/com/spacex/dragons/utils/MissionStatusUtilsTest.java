package com.spacex.dragons.utils;

import com.spacex.dragons.model.Mission;
import com.spacex.dragons.model.MissionStatus;
import com.spacex.dragons.model.Rocket;
import com.spacex.dragons.model.RocketStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.spacex.dragons.utils.MissionStatusUtils.calculateStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MissionStatusUtilsTest {


    @Test
    void shouldReturnEndedIfMissionIsEnded() {
        Mission mission = new Mission("M1");
        mission.setMissionStatus(MissionStatus.ENDED);
        assertEquals(MissionStatus.ENDED, calculateStatus(mission));
    }

    @Test
    void shouldReturnScheduledIfNoRocketsAssigned() {
        Mission mission = new Mission("M2");
        mission.setMissionStatus(MissionStatus.SCHEDULED);
        assertEquals(MissionStatus.SCHEDULED, calculateStatus(mission));
    }

    @Test
    void shouldReturnPendingIfAnyRocketIsInRepair() {
        Mission mission = new Mission("M3");
        Rocket r1 = new Rocket("R1");
        Rocket r2 = new Rocket("R2");

        r1.setRocketStatus(RocketStatus.IN_SPACE);
        r2.setRocketStatus(RocketStatus.IN_REPAIR);

        mission.setRockets(List.of(r1, r2));
        assertEquals(MissionStatus.PENDING, calculateStatus(mission));
    }

    @Test
    void shouldReturnInProgressIfAllRocketsAreNotInRepair() {
        Mission mission = new Mission("M4");
        Rocket r1 = new Rocket("R1");
        Rocket r2 = new Rocket("R2");

        r1.setRocketStatus(RocketStatus.ON_GROUND);
        r2.setRocketStatus(RocketStatus.IN_SPACE);

        mission.setRockets(List.of(r1, r2));
        assertEquals(MissionStatus.IN_PROGRESS, calculateStatus(mission));
    }

}
