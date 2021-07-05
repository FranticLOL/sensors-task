package com.tyulkov.sensors.service;

import com.tyulkov.sensors.entity.SensorMeasurement;
import com.tyulkov.sensors.repository.SensorMeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SensorMeasurementService {
    private final SensorMeasurementRepository repository;

    @Autowired
    public SensorMeasurementService(SensorMeasurementRepository repository) {
        this.repository = repository;
    }

    public void save(List<SensorMeasurement> sensorMeasurement) {
        for (SensorMeasurement measurement : sensorMeasurement) {
            repository.save(measurement);
        }
    }

    public List<SensorMeasurement> getMeasurementTimeBetween(Long sensorId, Long objectId, Long from, Long to) {
        return repository.findBySensorIdAndObjectIdAndTimeBetween(sensorId, objectId, from, to);
    }

    public List<SensorMeasurement> getLatestMeasurementByObjectId(Long objectId) {
        var sensorIds = repository.findSensorsIdsForObjectId(objectId);

        if(sensorIds.size() == 0 ) {
            return List.of();
        }

        var measurementList = new ArrayList<SensorMeasurement>();
        for (Long sensorId : sensorIds) {
            measurementList.add(repository.findTopByObjectIdAndSensorIdOrderByTimeDesc(objectId, sensorId));
        }

        return measurementList;
    }

    public Map<Long, Double> getLatestAverageForAllObjects() {
        var objectIds = repository.findDistinctObjectId();
        var avgMap = new HashMap<Long, Double>();
        for (Long objectId : objectIds) {
            var sensorIds = repository.findSensorsIdsForObjectId(objectId);

            if (sensorIds.size() == 0) {
                continue;
            }

            Double avg = 0.0;

            for (Long sensorId : sensorIds) {
                var measurement = repository.findTopByObjectIdAndSensorIdOrderByTimeDesc(objectId, sensorId);
                avg += measurement.getValue();
            }

            avg /= sensorIds.size();
            avgMap.put(objectId, avg);
        }

        return avgMap;
    }
}
