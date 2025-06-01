package com.spacex.dragons.service;

import com.spacex.dragons.model.Mission;
import com.spacex.dragons.model.MissionSummary;
import com.spacex.dragons.model.Rocket;
import com.spacex.dragons.repository.MissionRepository;
import com.spacex.dragons.repository.RocketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.spacex.dragons.model.MissionStatus.ENDED;
import static com.spacex.dragons.model.MissionStatus.IN_PROGRESS;
import static com.spacex.dragons.model.RocketStatus.IN_SPACE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SpaceXRocketServiceTest {

    private RocketRepository rocketRepository;
    private MissionRepository missionRepository;
    private SpaceXRocketService service;

    @BeforeEach
    void setup() {
        rocketRepository = mock(RocketRepository.class);
        missionRepository = mock(MissionRepository.class);
        service = new SpaceXRocketService(rocketRepository, missionRepository);
    }

    @Test
    void testAddRocket() {
        service.addRocket("R1");
        verify(rocketRepository, times(1)).addRocket("R1");
    }

    @Test
    void testAddMission() {
        service.addMission("M1");
        verify(missionRepository, times(1)).addMission("M1");
    }

    @Test
    void testAssignRocketToMission_Success() {
        Rocket rocket = new Rocket("R1");
        Mission mission = new Mission("M1");

        when(rocketRepository.getRocketById("R1")).thenReturn(rocket);
        when(missionRepository.getMissionById("M1")).thenReturn(mission);

        service.assignRocketToMission("R1", "M1");

        verify(rocketRepository).assignRocketToMission(rocket, mission);
        verify(missionRepository).assignRocketToMission(rocket, mission);
    }

    @Test
    void testAssignRocketToMission_RocketAlreadyAssigned() {
        Rocket rocket = new Rocket("R1");
        rocket.setMission(new Mission("ExistingMission"));

        when(rocketRepository.getRocketById("R1")).thenReturn(rocket);
        when(missionRepository.getMissionById("M1")).thenReturn(new Mission("M1"));

        assertThrows(IllegalStateException.class, () ->
                service.assignRocketToMission("R1", "M1"));
    }

    @Test
    void testAssignRocketToMission_MissionEnded() {
        Rocket rocket = new Rocket("R1");
        Mission mission = new Mission("M1");
        mission.setMissionStatus(ENDED);

        when(rocketRepository.getRocketById("R1")).thenReturn(rocket);
        when(missionRepository.getMissionById("M1")).thenReturn(mission);

        assertThrows(IllegalStateException.class, () ->
                service.assignRocketToMission("R1", "M1"));
    }

    @Test
    void testChangeRocketStatus_UpdatesMissionStatus() {
        Mission mission = new Mission("M1");
        Rocket rocket = new Rocket("R1");
        rocket.setMission(mission);
        mission.getRockets().add(rocket);

        when(rocketRepository.getRocketById("R1")).thenReturn(rocket);

        service.changeRocketStatus("R1", IN_SPACE);

        assertEquals(IN_SPACE, rocket.getRocketStatus());
        assertNotNull(mission.getMissionStatus());
    }

    @Test
    void testChangeMissionStatusToEnded_ClearsRockets() {
        Mission mission = new Mission("M1");
        Rocket rocket = new Rocket("R1");
        rocket.setRocketStatus(IN_SPACE);
        mission.getRockets().add(rocket);

        when(missionRepository.getMissionById("M1")).thenReturn(mission);

        service.changeMissionStatus("M1", ENDED);

        assertTrue(mission.getRockets().isEmpty());
        assertNull(rocket.getRocketStatus());
        assertEquals(ENDED, mission.getMissionStatus());
    }

    @Test
    void testGetMissionsSummary_SortedAndMapped() {
        Mission m1 = new Mission("M1");
        m1.setMissionStatus(IN_PROGRESS);
        m1.getRockets().add(new Rocket("R1"));
        m1.getRockets().add(new Rocket("R2"));

        Mission m2 = new Mission("M2");
        m2.setMissionStatus(IN_PROGRESS);
        m2.getRockets().add(new Rocket("R3"));

        when(missionRepository.getAllMissions()).thenReturn(List.of(m1, m2));

        SpaceXRocketService realService = new SpaceXRocketService(new RocketRepository(), missionRepository);
        List<MissionSummary> summaries = realService.getMissionsSummary();

        assertEquals(2, summaries.size());
        assertEquals("M1", summaries.get(0).getMissionId());
    }
}
