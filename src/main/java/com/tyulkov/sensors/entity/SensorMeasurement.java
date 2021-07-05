package com.tyulkov.sensors.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorMeasurement {
    @Id
    @GeneratedValue
    private Long id;

    private Long objectId;
    private Long sensorId;
    private Long time;
    private Double value;
}
