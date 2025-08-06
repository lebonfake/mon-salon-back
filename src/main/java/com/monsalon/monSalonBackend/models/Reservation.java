package com.monsalon.monSalonBackend.models;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;  // Creation date

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    // Many reservations belong to one salon
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salon_id")
    private Salon salon;

    // Many reservations belong to one client
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    // A reservation can have many services, and a service can belong to many reservations
    @ManyToMany
    @JoinTable(
            name = "reservation_service",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<Service> services = new HashSet<>();

    // Constructors
    public Reservation() {}

    public Reservation(LocalDate date, LocalDateTime startDate, LocalDateTime endDate, BigDecimal total, ReservationStatus status, Salon salon, Client client, Set<Service> services) {
        this.date = date;
        this.startDate = startDate;
        this.endDate = endDate;
        this.total = total;
        this.status = status;
        this.salon = salon;
        this.client = client;
        this.services = services;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public ReservationStatus getStatus() {
        return status;
    }
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Salon getSalon() {
        return salon;
    }
    public void setSalon(Salon salon) {
        this.salon = salon;
    }

    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Service> getServices() {
        return services;
    }
    public void setServices(Set<Service> services) {
        this.services = services;
    }
}
