package com.vtrsz.sistema_sensor.controller;

import com.vtrsz.sistema_sensor.dto.request.RequestWebSocketSensorDto;
import com.vtrsz.sistema_sensor.service.WebSocketSensorService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketSensorController {
    WebSocketSensorService webSocketSensorService;

    public WebSocketSensorController(WebSocketSensorService webSocketSensorService) {
        this.webSocketSensorService = webSocketSensorService;
    }

    @MessageMapping("/message")
    //@SendTo("/topic/public")
    public void receiveMessage(RequestWebSocketSensorDto requestWebSocketSensorDto, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        System.out.println("sendMessage: " + requestWebSocketSensorDto);

        //return JsonMapper.toJson("hello");
    }
}