package com.monsalon.monSalonBackend.Dto.planningRead;

import java.time.LocalTime;

public class RAvailablePeriodDto {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}