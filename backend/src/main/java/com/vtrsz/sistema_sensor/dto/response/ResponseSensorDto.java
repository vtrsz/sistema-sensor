package com.vtrsz.sistema_sensor.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ResponseSensorDto {
    private Long id;

    private String name;

    private UUID machineId;
}
