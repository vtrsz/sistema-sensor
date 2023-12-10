package com.vtrsz.sistema_sensor.dto;


import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class SensorMeasureDto implements Serializable {
    private int data;

    private String name;

    private Timestamp timestamp;
}
