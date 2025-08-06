package com.monsalon.monSalonBackend.models;

import jakarta.persistence.*;

@Entity
public class Client extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String name;

    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "salon_id")
    private Salon salon;


}
