package com.tyulkov.sensors.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyulkov.sensors.entity.SensorMeasurement;
import com.tyulkov.sensors.util.SensorMeasurementMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SensorMeasurementServiceTest {

    @Autowired
    SensorMeasurementService service;

    @Test
    void testSave() {
        var sm1 = SensorMeasurementMother.valid().build();
        var sm2 = SensorMeasurementMother.valid().objectId(2L).time(999L).build();
        var sm3 = SensorMeasurementMother.valid().time(100L).build();
        var sm4 = SensorMeasurementMother.valid().objectId(2L).time(100L).build();
        var sm5 = SensorMeasurementMother.valid().objectId(3L).time(998L).build();
        var sm6 = SensorMeasurementMother.valid().objectId(2L).time(1L).build();

        var list = List.of(sm1, sm2, sm3, sm4, sm5, sm6);
        service.save(list);
        for (SensorMeasurement measurement : list) {
            assertNotNull(measurement.getId());
        }
    }

    @Test
    void testGetMeasurementTimeBetween() {
        var sm1 = SensorMeasurementMother.valid().objectId(2L).time(100L).build();
        var sm2 = SensorMeasurementMother.valid().objectId(3L).time(998L).build();
        var sm3 = SensorMeasurementMother.valid().objectId(2L).time(1L).build();
        var list = List.of(sm1, sm2, sm3);

        service.save(list);
        var measurementBetween = service.getMeasurementTimeBetween(1L, 2L, 500L, 2000L);
        assertEquals(1, measurementBetween.size());
    }

    @Test
    void testGetLatestMeasurementByObjectId() {
        var sm1 = SensorMeasurementMother.valid().build();
        var sm2 = SensorMeasurementMother.valid().sensorId(2L).time(999L).build();
        var sm3 = SensorMeasurementMother.valid().time(100L).build();
        var sm4 = SensorMeasurementMother.valid().objectId(2L).time(100L).build();
        var sm5 = SensorMeasurementMother.valid().sensorId(2L).time(998L).build();
        var sm6 = SensorMeasurementMother.valid().sensorId(2L).time(1L).build();

        var list = List.of(sm1, sm2, sm3, sm4, sm5, sm6);
        service.save(list);

        var latestMeasurement = service.getLatestMeasurementByObjectId(1L);
        assertEquals(2, latestMeasurement.size());
        assertThat(latestMeasurement, containsInAnyOrder(sm1, sm2));
    }

    @Test
    void testGetLatestAverageForAllObjects() {
        var sm1 = SensorMeasurementMother.valid().value(12.0).build();
        var sm2 = SensorMeasurementMother.valid().sensorId(2L).time(999L).value(13.0).build();
        var sm3 = SensorMeasurementMother.valid().sensorId(3L).time(500L).value(14.0).build();
        var sm4 = SensorMeasurementMother.valid().sensorId(3L).time(100L).value(15.0).build();

        var list = List.of(sm1, sm2, sm3, sm4);
        service.save(list);

        var map = service.getLatestAverageForAllObjects();
        assertEquals(13.0, map.get(1L));
    }

    @Test
    void testBigData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = SensorMeasurement.class.getResourceAsStream("/data.json");
        var list = mapper.readValue(is, new TypeReference<List<SensorMeasurement>>() {
        });
        service.save(list);
        for (SensorMeasurement measurement : list) {
            assertNotNull(measurement.getId());
        }
    }
}