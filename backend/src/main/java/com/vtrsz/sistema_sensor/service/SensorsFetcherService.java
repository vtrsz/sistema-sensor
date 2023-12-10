package com.vtrsz.sistema_sensor.service;

import com.vtrsz.sistema_sensor.dto.SensorMeasureDto;
import com.vtrsz.sistema_sensor.entity.Sensor;
import com.vtrsz.sistema_sensor.entity.SensorHistory;
import com.vtrsz.sistema_sensor.repository.SensorHistoryRepository;
import com.vtrsz.sistema_sensor.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class SensorsFetcherService {
    private final SensorRepository sensorRepository;
    private final SensorHistoryRepository sensorHistoryRepository;

    private final WebSocketSensorService webSocketSensorService;

    @Value("${external.app.protocol}")
    private String protocol;
    @Value("${external.app.host}")
    private String host;
    @Value("${external.app.port}")
    private String port;

    public SensorsFetcherService(SensorRepository sensorRepository,
                                 SensorHistoryRepository sensorHistoryRepository,
                                 WebSocketSensorService webSocketSensorService) {
        this.sensorRepository = sensorRepository;
        this.sensorHistoryRepository = sensorHistoryRepository;
        this.webSocketSensorService = webSocketSensorService;
    }

    @Scheduled(fixedRate = 300) // 300ms interval
    public void callExternalApiAndSaveToDatabase() {
        final String apiUrl = String.format("%s://%s:%s/api/v1/sensors", this.protocol, this.host, this.port);

        RestTemplate restTemplate = new RestTemplate();

        for (Sensor sensor : sensorRepository.findAll()) {
            String name = sensor.getName();

            try {
                SensorMeasureDto sensorMeasureDto = restTemplate.getForObject(String.format("%s?name=%s", apiUrl, name), SensorMeasureDto.class);

                SensorHistory sensorHistory = SensorHistory.builder()
                        .data(sensorMeasureDto.getData())
                        .timestamp(sensorMeasureDto.getTimestamp())
                        .sensor(sensor)
                        .build();

                sensorHistoryRepository.save(sensorHistory);

                webSocketSensorService.broadcastMessage(sensor, sensorMeasureDto);
            } catch (Exception ex) {
                System.out.println("Cannot get the sensor measure data: " + name);
                System.out.println(Arrays.toString(ex.getStackTrace()));
            }
        }
    }
}
