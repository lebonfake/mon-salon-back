package com.monsalon.monSalonBackend.Dto.reservationReadDto;


import com.monsalon.monSalonBackend.models.ReservationStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class ReservationReadDto {
    private Long id;
    private LocalDate date; // Creation date of the reservation record
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal total;
    private ReservationStatus status; // Enum

    private ClientInReservationDto client; // Simplified Client DTO
    private Set<ServiceInReservationDto> services; // Simplified Services DTOs

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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



    public ClientInReservationDto getClient() {
        return client;
    }

    public void setClient(ClientInReservationDto client) {
        this.client = client;
    }

    public Set<ServiceInReservationDto> getServices() {
        return services;
    }

    public void setServices(Set<ServiceInReservationDto> services) {
        this.services = services;
    }
}