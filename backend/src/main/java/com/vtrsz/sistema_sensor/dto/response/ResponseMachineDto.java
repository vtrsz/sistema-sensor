package com.vtrsz.sistema_sensor.dto.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ResponseMachineDto {
    private UUID id;

    private List<ResponseSensorAttachedToMachineDto> sensors;

    private Long sequence;
}
