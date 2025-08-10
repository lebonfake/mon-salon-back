package com.monsalon.monSalonBackend.Dto.planningRead;


import java.util.List;

public class WholeScheduleDto {
    private Long id;
    private String name;
    private boolean currentlyUsed;
    private List<RScheduleDto> schedules;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isCurrentlyUsed() { return currentlyUsed; }
    public void setCurrentlyUsed(boolean currentlyUsed) { this.currentlyUsed = currentlyUsed; }
    public List<RScheduleDto> getSchedules() { return schedules; }
    public void setSchedules(List<RScheduleDto> schedules) { this.schedules = schedules; }
}
