package com.anotherdeveloper.zacierajto.models;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by Marcin on 2017-06-01.
 */

public class BrewingConfiguration {
    private double setpoint;
    private int mashingLevels;
    private Map<Integer, StageParameters> stageParameterses = new Map<Integer, StageParameters>() {
        @Override
        public int size() {
            return mashingLevels;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean containsKey(Object o) {
            return false;
        }

        @Override
        public boolean containsValue(Object o) {
            return false;
        }

        @Override
        public StageParameters get(Object o) {
            return null;
        }

        @Override
        public StageParameters put(Integer integer, StageParameters stageParameters) {
            return null;
        }

        @Override
        public StageParameters remove(Object o) {
            return null;
        }

        @Override
        public void putAll(Map<? extends Integer, ? extends StageParameters> map) {

        }

        @Override
        public void clear() {

        }

        @NonNull
        @Override
        public Set<Integer> keySet() {
            return null;
        }

        @NonNull
        @Override
        public Collection<StageParameters> values() {
            return null;
        }

        @NonNull
        @Override
        public Set<Entry<Integer, StageParameters>> entrySet() {
            return null;
        }
    };

    public double getSetpoint() {
        return setpoint;
    }

    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
    }

    public int getMashingLevels() {
        return mashingLevels;
    }

    public void setMashingLevels(int mashingLevels) {
        this.mashingLevels = mashingLevels;
    }
}
