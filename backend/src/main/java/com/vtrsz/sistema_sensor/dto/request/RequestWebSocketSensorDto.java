package com.vtrsz.sistema_sensor.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RequestWebSocketSensorDto implements Serializable {
    @JsonProperty
    private String id;
}
