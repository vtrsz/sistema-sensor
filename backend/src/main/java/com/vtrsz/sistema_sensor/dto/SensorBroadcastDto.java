package com.vtrsz.sistema_sensor.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class SensorBroadcastDto {
    private Long id;

    private int data;

    private String name;

    private Timestamp timestamp;
}
