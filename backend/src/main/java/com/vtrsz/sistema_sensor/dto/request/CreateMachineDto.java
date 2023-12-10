package com.vtrsz.sistema_sensor.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreateMachineDto {
    @NotNull
    private Long sequence;
}
