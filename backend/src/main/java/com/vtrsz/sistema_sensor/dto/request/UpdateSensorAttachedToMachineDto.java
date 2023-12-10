package com.vtrsz.sistema_sensor.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UpdateSensorAttachedToMachineDto {
    @NotBlank
    private String name;
}