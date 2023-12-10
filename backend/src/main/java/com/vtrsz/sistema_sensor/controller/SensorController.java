package com.vtrsz.sistema_sensor.controller;

import com.vtrsz.sistema_sensor.dto.request.CreateSensorDto;
import com.vtrsz.sistema_sensor.dto.response.ResponseMachineDto;
import com.vtrsz.sistema_sensor.dto.response.ResponseSensorDto;
import com.vtrsz.sistema_sensor.exception.BusinessRuleException;
import com.vtrsz.sistema_sensor.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Sensor Controller", description = "Create, Update, Read and Delete")
@RestController
@RequestMapping(path = "/api/sensor", produces = {"application/json"})
public class SensorController {
    SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Operation(summary = "Create sensor",
            description = "Create a sensor and save to database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseSensorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json")),
    })
    @PostMapping
    public ResponseEntity<ResponseSensorDto> post(@Valid @RequestBody CreateSensorDto sensorDto) throws BusinessRuleException {
        return new ResponseEntity<>(sensorService.create(sensorDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get sensor",
            description = "Get a sensor information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Got", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResponseSensorDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(path = "/{id}", produces = {"application/json"})
    public ResponseEntity<ResponseSensorDto> get(@PathVariable final Long id) {
        return ResponseEntity.of(sensorService.getById(id));
    }

    @Operation(summary = "Get all sensors",
            description = "Get a list of sensors information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Got", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseSensorDto.class))),
    })
    @GetMapping(produces = {"application/json"})
    public ResponseEntity<List<ResponseSensorDto>> getAll() {
        return ResponseEntity.of(Optional.ofNullable(sensorService.getAll()));
    }

    @Operation(summary = "Update sensor",
            description = "Update a sensor and save to database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMachineDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(mediaType = "application/json"))
    })
    @PutMapping(path = "/{id}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<ResponseSensorDto> put(@PathVariable final Long id, @Valid @RequestBody CreateSensorDto updateSensorDto) throws BusinessRuleException {
        return ResponseEntity.of(sensorService.updateById(updateSensorDto, id));
    }

    @Operation(summary = "Delete sensor",
            description = "Remove a sensor from database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Deleted"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable final Long id) {
        return ResponseEntity.of(sensorService.delete(id));
    }
}
