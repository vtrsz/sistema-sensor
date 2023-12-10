package com.vtrsz.sistema_sensor.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ResponseSensorAttachedToMachineDto {
    private Long id;

    private String name;
}
