package com.monsalon.monSalonBackend.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Schedule extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int dayOfWeek;
    private int maxConcurrentBookings;

    @OneToOne
    @JoinColumn(name = "salon_id")
    private Salon salon;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvailabalePeriod> availabalePeriods;



}
