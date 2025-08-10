package com.monsalon.monSalonBackend.Dto.reservationCreateDto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.Set;

public class ReservationCreateDto {

    @NotNull(message = "L'heure de début est obligatoire.")
    @FutureOrPresent(message = "L'heure de début doit être future ou présente.")
    private LocalDateTime startTime;

    @NotNull(message = "L'heure de fin est obligatoire.")
    private LocalDateTime endTime;

    private String clientName;
    private String clientPhoneNumber;

    private Long clientId; // Client is identified by ID

    @NotNull(message = "Au moins un service est requis.")
    private Set<Long> serviceIds; // Services are identified by IDs

    // Getters and Setters
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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Set<Long> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(Set<Long> serviceIds) {
        this.serviceIds = serviceIds;
    };

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public boolean hasClientId() {
        if(clientId != null)
            return true;
        return false;
    }

    public boolean hasNewClientInfo() {
        if(clientName != null && clientPhoneNumber != null)
            return true;
        return false;
    }
}
