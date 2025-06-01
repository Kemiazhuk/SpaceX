package com.spacex.dragons.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rocket {
    private RocketStatus rocketStatus;
    private String rocketId;
    private Mission mission;
    public Rocket(String rocketId){
        this.rocketId = rocketId;
        this.rocketStatus = RocketStatus.ON_GROUND;
    }
}
