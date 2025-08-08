package com.monsalon.monSalonBackend.models;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int durationInMinutes;

    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salon_id")
    private Salon salon;

    @ManyToMany(mappedBy = "services",fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    // Constructors
    public Services() {
    }

    public Services(String name, int durationInMinutes, BigDecimal price, Salon salon) {
        this.name = name;
        this.durationInMinutes = durationInMinutes;
        this.price = price;
        this.salon = salon;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Salon getSalon() {
        return salon;
    }

    public void setSalon(Salon salon) {
        this.salon = salon;
    }
}

