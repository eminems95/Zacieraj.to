package com.anotherdeveloper.zacierajto.models;

import java.util.Date;

/**
 * Created by Marcin on 2017-06-01.
 */

public class StageParameters {
    private double setpoint;
    private int minutes;

    public int getMinutes() {
        return minutes;
    }

    public double getSetpoint() {
        return setpoint;
    }

    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
