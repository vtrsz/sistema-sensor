package com.vtrsz.sistema_sensor.repository;

import com.vtrsz.sistema_sensor.entity.SensorHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SensorHistoryRepository extends JpaRepository<SensorHistory, UUID> {
    void deleteBySensorId(Long id);
}