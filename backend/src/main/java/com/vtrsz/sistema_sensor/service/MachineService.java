package com.vtrsz.sistema_sensor.service;

import com.vtrsz.sistema_sensor.dto.request.CreateMachineDto;
import com.vtrsz.sistema_sensor.dto.request.UpdateMachineDto;
import com.vtrsz.sistema_sensor.dto.response.ResponseMachineDto;
import com.vtrsz.sistema_sensor.dto.response.ResponseSensorAttachedToMachineDto;
import com.vtrsz.sistema_sensor.entity.Machine;
import com.vtrsz.sistema_sensor.entity.Sensor;
import com.vtrsz.sistema_sensor.repository.MachineRepository;
import com.vtrsz.sistema_sensor.repository.SensorHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MachineService {
    MachineRepository machineRepository;

    SensorHistoryRepository sensorHistoryRepository;

    public MachineService(MachineRepository machineRepository, SensorHistoryRepository sensorHistoryRepository) {
        this.machineRepository = machineRepository;
        this.sensorHistoryRepository = sensorHistoryRepository;
    }

    private static ResponseMachineDto machineToResponseDto(Machine machine) {
        return ResponseMachineDto.builder()
                .id(machine.getId())
                .sequence(machine.getSequence())
                .sensors(machine.getSensors().stream().map((sensor) -> {
                    if (sensor != null) {
                        return ResponseSensorAttachedToMachineDto.builder()
                                .id(sensor.getId())
                                .name(sensor.getName())
                                .build();
                    }
                    return null;
                }).collect(Collectors.toList()))
                .build();
    }

    public ResponseMachineDto create(CreateMachineDto machineDto) {
        if (machineDto == null) {
            throw new IllegalArgumentException("a person cannot be null");
        }

        Machine machine = Machine.builder()
                .sequence(machineDto.getSequence())
                .build();

        machineRepository.save(machine);

        if (machine.getSensors() == null || machine.getSensors().isEmpty()) {
            return ResponseMachineDto.builder()
                    .id(machine.getId())
                    .sequence(machine.getSequence())
                    .sensors(Collections.emptyList())
                    .build();
        }

        return MachineService.machineToResponseDto(machine);
    }

    public Optional<ResponseMachineDto> getById(final UUID id) {
        Optional<Machine> searchedMachine = machineRepository.findById(id);

        return searchedMachine.map(MachineService::machineToResponseDto);
    }

    public List<ResponseMachineDto> getAll() {
        List<Machine> machines = machineRepository.findAllByOrderByIdAsc();

        if (machines.isEmpty()) {
            return Collections.emptyList();
        }

        List<ResponseMachineDto> responseMachines = new ArrayList<>();

        for (Machine machine : machines) {
            ResponseMachineDto machineDto = MachineService.machineToResponseDto(machine);
            responseMachines.add(machineDto);
        }

        return responseMachines;
    }

    @Transactional
    public Optional<ResponseMachineDto> updateById(UpdateMachineDto machineDto, UUID id) {
        Optional<Machine> machineToUpdate = machineRepository.findById(id);
        if (machineToUpdate.isEmpty()) {
            return Optional.empty();
        }

        machineToUpdate.get().setSequence(machineDto.getSequence());

        List<Sensor> sensors = machineToUpdate.get().getSensors();

        for (Sensor sensor : sensors) {
            sensorHistoryRepository.deleteBySensorId(sensor.getId());
        }

        sensors.clear();

        sensors.addAll(machineDto.getSensors().stream().map(sensorDto -> Sensor.builder()
                .name(sensorDto.getName())
                .machine(machineToUpdate.get())
                .build()).toList());

        machineRepository.save(machineToUpdate.get());

        return Optional.ofNullable(MachineService.machineToResponseDto(machineToUpdate.get()));
    }
}
