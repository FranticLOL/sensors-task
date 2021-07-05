package com.tyulkov.sensors.controller;

import com.tyulkov.sensors.entity.SensorMeasurement;
import com.tyulkov.sensors.service.SensorMeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SensorMeasurementController {
    private final SensorMeasurementService service;

    @Autowired
    SensorMeasurementController(SensorMeasurementService service) {
        this.service = service;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/save")
    public void saveMeasurement(@RequestBody List<SensorMeasurement> sensorMeasurement) {
        service.save(sensorMeasurement);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/history")
    public List<SensorMeasurement> getObjectHistoryBetween(@RequestParam Long sensorId, Long objectId, Long from, Long to) {
        return service.getMeasurementTimeBetween(sensorId, objectId, from, to);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/latest")
    public List<SensorMeasurement> getLatestMeasurementByObjectId(@RequestParam Long objectId) {
        return service.getLatestMeasurementByObjectId(objectId);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/avg")
    public Map<Long, Double> getLatestAverageForAllObjects() {
        return service.getLatestAverageForAllObjects();
    }
}
