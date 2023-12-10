package com.vtrsz.sistema_sensor.service;

import com.vtrsz.sistema_sensor.dto.request.CreateSensorDto;
import com.vtrsz.sistema_sensor.dto.request.UpdateMachineDto;
import com.vtrsz.sistema_sensor.dto.response.ResponseMachineDto;
import com.vtrsz.sistema_sensor.dto.response.ResponseSensorDto;
import com.vtrsz.sistema_sensor.entity.Machine;
import com.vtrsz.sistema_sensor.entity.Sensor;
import com.vtrsz.sistema_sensor.exception.BusinessRuleException;
import com.vtrsz.sistema_sensor.repository.MachineRepository;
import com.vtrsz.sistema_sensor.repository.SensorHistoryRepository;
import com.vtrsz.sistema_sensor.repository.SensorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SensorService {
    MachineRepository machineRepository;
    SensorRepository sensorRepository;
    SensorHistoryRepository sensorHistoryRepository;

    public SensorService(MachineRepository machineRepository, SensorRepository sensorRepository, SensorHistoryRepository sensorHistoryRepository) {
        this.machineRepository = machineRepository;
        this.sensorRepository = sensorRepository;
        this.sensorHistoryRepository = sensorHistoryRepository;
    }

    private static ResponseSensorDto sensorToResponseDto(Sensor sensor) {
        return ResponseSensorDto.builder()
                .id(sensor.getId())
                .name(sensor.getName())
                .machineId(sensor.getMachine().getId())
                .build();
    }

    public ResponseSensorDto create(CreateSensorDto sensorDto) throws BusinessRuleException {
        Optional<Machine> machine = machineRepository.findById(sensorDto.getMachineId());
        if (machine.isEmpty()) {
            throw new BusinessRuleException("a machine with this machineId is not found");
        }

        Sensor sensor = Sensor.builder()
                .name(sensorDto.getName())
                .machine(machine.get())
                .build();

        sensorRepository.save(sensor);

        return SensorService.sensorToResponseDto(sensor);
    }

    public Optional<ResponseSensorDto> getById(final Long id) {
        Optional<Sensor> searchedSensor = sensorRepository.findById(id);

        return searchedSensor.map(SensorService::sensorToResponseDto);
    }

    public List<ResponseSensorDto> getAll() {
        List<Sensor> sensors = sensorRepository.findAllByOrderByIdAsc();

        if (sensors.isEmpty()) {
            return Collections.emptyList();
        }

        List<ResponseSensorDto> responseSensor = new ArrayList<>();

        for (Sensor sensor : sensors) {
            ResponseSensorDto sensorDto = SensorService.sensorToResponseDto(sensor);
            responseSensor.add(sensorDto);
        }

        return responseSensor;
    }

    @Transactional
    public Optional<ResponseSensorDto> updateById(CreateSensorDto sensorDto, Long id) throws BusinessRuleException {
        Optional<Sensor> sensorToUpdate = sensorRepository.findById(id);
        if (sensorToUpdate.isEmpty()) {
            return Optional.empty();
        }

        Optional<Machine> machine = machineRepository.findById(sensorDto.getMachineId());
        if (machine.isEmpty()) {
            throw new BusinessRuleException("cannot find a machine with this machineId");
        }

        sensorToUpdate.get().setName(sensorDto.getName());
        sensorToUpdate.get().setMachine(machine.get());

        //sensorHistoryRepository.deleteBySensorId(sensor.getId());

        sensorRepository.save(sensorToUpdate.get());

        return Optional.ofNullable(SensorService.sensorToResponseDto(sensorToUpdate.get()));
    }
}
