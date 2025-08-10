package com.monsalon.monSalonBackend.Dto.planningCreation;

import java.util.List;

public class RequestPlanningDto {
    private String name;

    private boolean currentlyUsed;

    private List<ScheduleDto> scheduleDtoList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCurrentlyUsed() {
        return currentlyUsed;
    }

    public void setCurrentlyUsed(boolean currentlyUsed) {
        this.currentlyUsed = currentlyUsed;
    }

    public List<ScheduleDto> getScheduleDtoList() {
        return scheduleDtoList;
    }

    public void setScheduleDtoList(List<ScheduleDto> scheduleDtoList) {
        this.scheduleDtoList = scheduleDtoList;
    }
}
