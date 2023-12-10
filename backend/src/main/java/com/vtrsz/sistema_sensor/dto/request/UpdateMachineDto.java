package com.vtrsz.sistema_sensor.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UpdateMachineDto {
    @NotNull
    private List<UpdateSensorAttachedToMachineDto> sensors;

    @NotNull
    private Long sequence;
}
