package com.vtrsz.sistema_sensor.repository;

import com.vtrsz.sistema_sensor.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
    List<Sensor> findAllByOrderByIdAsc();
}