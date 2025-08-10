package com.monsalon.monSalonBackend.Dto.servicesCreation;


import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ServiceCreateDto {

    @NotBlank(message = "Le nom du service est obligatoire.")
    private String name;

    @Min(value = 1, message = "La durée doit être d'au moins 1 minute.")
    @NotNull(message = "La durée est obligatoire.")
    private Integer durationInMinutes; // Use Integer for nullable validation

    @DecimalMin(value = "0.00", message = "Le prix ne peut pas être négatif.")
    @NotNull(message = "Le prix est obligatoire.")
    private BigDecimal price;

    // Constructors
    public ServiceCreateDto() {
    }

    public ServiceCreateDto(String name, Integer durationInMinutes, BigDecimal price) {
        this.name = name;
        this.durationInMinutes = durationInMinutes;
        this.price = price;
    }

    // Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}