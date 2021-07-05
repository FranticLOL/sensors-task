package com.tyulkov.sensors.controller;

import com.tyulkov.sensors.entity.SensorMeasurement;
import com.tyulkov.sensors.service.SensorMeasurementService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class SensorMeasurementControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SensorMeasurementService service;

    @Captor
    private ArgumentCaptor<List<SensorMeasurement>> captor;

    @Test
    void saveMeasurement() throws Exception {
        mvc.perform(
                post("/api/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"objectId\": 1,\"sensorId\": 1,\"time\": 1565796600,\"value\": 9.4}]")
        ).andExpect(status().isOk());

        verify(service).save(captor.capture());

        var measurements = captor.getValue();
        assertEquals(1, measurements.size());
        assertEquals(1L, measurements.get(0).getObjectId());
    }

    @Test
    void getObjectHistoryBetween() throws Exception {
        mvc.perform(
                get("/api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("sensorId", "3")
                        .param("objectId", "1")
                        .param("from", "1565796000")
                        .param("to", "1565796001")
        ).andExpect(status().isOk());
    }

    @Test
    void getLatestMeasurementByObjectId() throws Exception {
        mvc.perform(
                get("/api/latest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("objectId", "1")
        ).andExpect(status().isOk());
    }

    @Test
    void getLatestAverageForAllObjects() throws Exception {
        mvc.perform(
                get("/api/avg")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}