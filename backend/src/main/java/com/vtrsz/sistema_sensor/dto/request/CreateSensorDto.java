package com.vtrsz.sistema_sensor.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreateSensorDto {
    @NotBlank
    private String name;

    @NotNull
    private UUID machineId;
}