package com.spacex.dragons.service;

import com.spacex.dragons.model.Mission;
import com.spacex.dragons.model.MissionStatus;
import com.spacex.dragons.model.MissionSummary;
import com.spacex.dragons.model.Rocket;
import com.spacex.dragons.model.RocketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SpaceXRocketServiceTest {

    private SpaceXRocketService service;

    @BeforeEach
    void setUp() {
        service = new SpaceXRocketService();
    }

    @Test
    public void testAddRocket() {
        Rocket rocket = service.addRocket("DragonX");
        assertNotNull(rocket);
        assertEquals("DragonX", rocket.getRocketId());
        assertEquals(RocketStatus.ON_GROUND, rocket.getRocketStatus());
    }

    @Test
    public void testAddMission() {
        Mission mission = service.addMission("LunaMission");
        assertNotNull(mission);
        assertEquals("LunaMission", mission.getMissionId());
        assertEquals(MissionStatus.SCHEDULED, mission.getMissionStatus());
    }

    @Test
    public void testAssignRocketToMission_Success() {
        service.addRocket("Dragon1");
        service.addMission("Mars");

        service.assignRocketToMission("Dragon1", "Mars");

        List<MissionSummary> summaries = service.getMissionsSummary();
        assertEquals(1, summaries.size());
        assertEquals("Mars", summaries.get(0).getMissionId());
        assertEquals(1, summaries.get(0).getRocketAmount());
        assertEquals("In Progress", summaries.get(0).getStatus());
    }

    @Test
    public void testAssignRocketToMission_RocketAlreadyAssigned() {
        service.addRocket("Dragon1");
        service.addRocket("Dragon2");
        service.addMission("Moon");

        service.assignRocketToMission("Dragon1", "Moon");

        Exception ex = assertThrows(IllegalStateException.class, () -> {
            service.assignRocketToMission("Dragon1", "Moon");
        });

        assertEquals("Rocket already assigned", ex.getMessage());
    }

    @Test
    public void testChangeRocketStatus_UpdatesMissionStatus() {
        service.addRocket("DragonR");
        service.addMission("MissionR");

        service.assignRocketToMission("DragonR", "MissionR");
        service.changeRocketStatus("DragonR", RocketStatus.IN_REPAIR);

        List<MissionSummary> summaries = service.getMissionsSummary();
        assertEquals("Pending", summaries.get(0).getStatus());
    }

    @Test
    public void testChangeMissionStatusToEnded() {
        service.addRocket("Dragon9");
        service.addMission("LunarX");

        service.assignRocketToMission("Dragon9", "LunarX");

        service.changeMissionStatus("LunarX", MissionStatus.ENDED);

        List<MissionSummary> summaries = service.getMissionsSummary();
        assertEquals(0, summaries.get(0).getRocketAmount());
        assertEquals("Ended", summaries.get(0).getStatus());
    }

    @Test
    public void testMissionSummaryOrdering() {
        service.addRocket("A");
        service.addRocket("B");
        service.addRocket("C");
        service.addMission("Zeta");
        service.addMission("Alpha");

        service.assignRocketToMission("A", "Zeta");
        service.assignRocketToMission("B", "Zeta");
        service.assignRocketToMission("C", "Alpha");

        List<MissionSummary> summaries = service.getMissionsSummary();

        assertEquals("Zeta", summaries.get(0).getMissionId());
        assertEquals("Alpha", summaries.get(1).getMissionId());
    }
}
