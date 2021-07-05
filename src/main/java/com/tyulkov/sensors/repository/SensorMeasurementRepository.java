package com.tyulkov.sensors.repository;

import com.tyulkov.sensors.entity.SensorMeasurement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorMeasurementRepository extends CrudRepository<SensorMeasurement, Long> {
    List<SensorMeasurement> findBySensorIdAndObjectIdAndTimeBetween(Long sensorId, Long objectId, Long from, Long to);

    @Query("Select distinct sm.sensorId from SensorMeasurement sm where sm.objectId = ?1")
    List<Long> findSensorsIdsForObjectId(Long objectId);

    SensorMeasurement findTopByObjectIdAndSensorIdOrderByTimeDesc(Long objectId, Long sensorId);

    @Query("Select distinct sm.objectId from SensorMeasurement sm")
    List<Long> findDistinctObjectId();
}
