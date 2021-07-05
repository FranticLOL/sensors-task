package com.tyulkov.sensors.util;

import com.tyulkov.sensors.entity.SensorMeasurement;

public class SensorMeasurementMother {
    public static SensorMeasurement.SensorMeasurementBuilder valid() {
        return SensorMeasurement.builder()
                .objectId(1L)
                .sensorId(1L)
                .time(1000L)
                .value(21.3);
    }
}
