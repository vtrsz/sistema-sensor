package com.vtrsz.sistema_sensor.controller;

import com.vtrsz.sistema_sensor.dto.request.CreateMachineDto;
import com.vtrsz.sistema_sensor.dto.request.UpdateMachineDto;
import com.vtrsz.sistema_sensor.dto.response.ResponseMachineDto;
import com.vtrsz.sistema_sensor.service.MachineService;
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
import java.util.UUID;

@Tag(name = "Machine Controller", description = "Create, Update, Read and Delete")
@RestController
@RequestMapping(path = "/api/machine", produces = {"application/json"})
public class MachineController {
    MachineService machineService;

    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @Operation(summary = "Create machine",
            description = "Create a machine and save to database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMachineDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json")),
    })
    @PostMapping
    public ResponseEntity<ResponseMachineDto> post(@Valid @RequestBody CreateMachineDto machineDto) {
        return new ResponseEntity<>(machineService.create(machineDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get machine",
            description = "Get a machine information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Got", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResponseMachineDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(path = "/{id}", produces = {"application/json"})
    public ResponseEntity<ResponseMachineDto> get(@PathVariable final UUID id) {
        return ResponseEntity.of(machineService.getById(id));
    }

    @Operation(summary = "Get all machines",
            description = "Get a list of machine information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Got", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMachineDto.class))),
    })
    @GetMapping(produces = {"application/json"})
    public ResponseEntity<List<ResponseMachineDto>> getAll() {
        return ResponseEntity.of(Optional.ofNullable(machineService.getAll()));
    }

    @Operation(summary = "Update machine",
            description = "Update a machine and save to database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMachineDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(mediaType = "application/json"))
    })
    @PutMapping(path = "/{id}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<ResponseMachineDto> putPerson(@PathVariable final UUID id, @Valid @RequestBody UpdateMachineDto updateMachineDto) {
        return ResponseEntity.of(machineService.updateById(updateMachineDto, id));
    }
}
