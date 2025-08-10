package com.monsalon.monSalonBackend.Dto.servicesCreation;


import java.math.BigDecimal;

public class ServiceReadDto {

    private Long id;
    private String name;
    private int durationInMinutes;
    private BigDecimal price;

    // Constructors
    public ServiceReadDto() {
    }

    public ServiceReadDto(Long id, String name, int durationInMinutes, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.durationInMinutes = durationInMinutes;
        this.price = price;

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


}
