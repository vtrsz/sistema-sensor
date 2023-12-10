package com.vtrsz.sistema_sensor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity(name = "sensor_history")
@Table(name = "sensor_history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SensorHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    private Sensor sensor;

    @Column(nullable = false)
    private int data;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Timestamp timestamp;
}
