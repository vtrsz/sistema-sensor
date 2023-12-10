package com.vtrsz.sistema_sensor.repository;

import com.vtrsz.sistema_sensor.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MachineRepository extends JpaRepository<Machine, UUID> {
    List<Machine> findAllByOrderByIdAsc();
}