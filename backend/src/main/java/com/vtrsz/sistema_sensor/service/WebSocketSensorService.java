package com.vtrsz.sistema_sensor.service;

import com.vtrsz.sistema_sensor.dto.SensorBroadcastDto;
import com.vtrsz.sistema_sensor.dto.SensorMeasureDto;
import com.vtrsz.sistema_sensor.entity.Sensor;
import com.vtrsz.sistema_sensor.mapper.JsonMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketSensorService {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketSensorService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastMessage(Sensor sensor, SensorMeasureDto sensorMeasureDto) throws Exception {
        SensorBroadcastDto sensorBroadcastDto = SensorBroadcastDto.builder()
                .id(sensor.getId())
                .data(sensorMeasureDto.getData())
                .name(sensorMeasureDto.getName())
                .timestamp(sensorMeasureDto.getTimestamp())
                .build();

        messagingTemplate.convertAndSend("/topic/public", JsonMapper.toJson(sensorBroadcastDto));
    }
}
