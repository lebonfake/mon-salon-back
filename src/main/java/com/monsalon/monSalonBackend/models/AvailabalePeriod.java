package com.monsalon.monSalonBackend.models;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class AvailabalePeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

}
