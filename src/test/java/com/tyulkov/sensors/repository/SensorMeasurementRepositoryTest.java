package com.tyulkov.sensors.repository;

import com.tyulkov.sensors.entity.SensorMeasurement;
import com.tyulkov.sensors.util.SensorMeasurementMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SensorMeasurementRepositoryTest {

    @Autowired
    SensorMeasurementRepository repository;

    @Test
    void testSaveSensorMeasurement() {
        var measurement = SensorMeasurement.builder().objectId(1L).sensorId(1L).time(1200L).value(10.0).build();
        repository.save(measurement);

        assertNotNull(measurement.getId());
    }

    @Test
    void testFindBySensorIdAndObjectIdAndTimeBetween() {
        var measurement1 = SensorMeasurement.builder().objectId(1L).sensorId(1L).time(1000L).value(10.0).build();
        var measurement2 = SensorMeasurement.builder().objectId(1L).sensorId(1L).time(1200L).value(10.0).build();
        var measurement3 = SensorMeasurement.builder().objectId(1L).sensorId(1L).time(4200L).value(10.0).build();

        repository.saveAll(List.of(measurement1, measurement2, measurement3));

        assertEquals( 2, repository.findBySensorIdAndObjectIdAndTimeBetween(1L, 1L, 900L, 2000L).size());
    }

    @Test
    void testFindSensorsIdsForObjectId() {
        var sm1 = SensorMeasurementMother.valid().build();
        var sm2 = SensorMeasurementMother.valid().sensorId(2L).time(999L).build();
        var sm3 = SensorMeasurementMother.valid().time(100L).build();
        var sm4 = SensorMeasurementMother.valid().objectId(2L).time(100L).build();

        var list = List.of(sm1,sm2,sm3,sm4);
        repository.saveAll(list);

        var sensors = repository.findSensorsIdsForObjectId(1L);
        assertEquals(2, sensors.size());

        assertThat(sensors, containsInAnyOrder(1L,2L));
    }

    @Test
    void testFindTopByObjectIdAndSensorIdOrderByTimeDesc() {
        var sm1 = SensorMeasurementMother.valid().build();
        var sm2 = SensorMeasurementMother.valid().sensorId(2L).time(999L).build();
        var sm3 = SensorMeasurementMother.valid().time(100L).build();
        var sm4 = SensorMeasurementMother.valid().objectId(2L).time(100L).build();
        var sm5 = SensorMeasurementMother.valid().sensorId(2L).time(998L).build();
        var sm6 = SensorMeasurementMother.valid().sensorId(2L).time(1L).build();

        var list = List.of(sm1,sm2,sm3,sm4,sm5,sm6);
        repository.saveAll(list);

        var resultSm = repository.findTopByObjectIdAndSensorIdOrderByTimeDesc(1L,2L);
        assertNotNull(resultSm);
        assertEquals(999L,resultSm.getTime());
    }

    @Test
    void testFindDistinctByObjectId() {
        var sm1 = SensorMeasurementMother.valid().build();
        var sm2 = SensorMeasurementMother.valid().objectId(2L).time(999L).build();
        var sm3 = SensorMeasurementMother.valid().time(100L).build();
        var sm4 = SensorMeasurementMother.valid().objectId(2L).time(100L).build();
        var sm5 = SensorMeasurementMother.valid().objectId(3L).time(998L).build();
        var sm6 = SensorMeasurementMother.valid().objectId(2L).time(1L).build();

        var list = List.of(sm1,sm2,sm3,sm4,sm5,sm6);
        repository.saveAll(list);

        assertEquals(3, repository.findDistinctObjectId().size());
    }
}