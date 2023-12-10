package com.vtrsz.sistema_sensor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "sensor")
@Table(name = "sensor")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "machine_id", referencedColumnName = "id")
    private Machine machine;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SensorHistory> sensorHistoryList = new ArrayList<>();
}
