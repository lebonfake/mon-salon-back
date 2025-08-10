package com.monsalon.monSalonBackend.Dto.planningCreation;

import java.util.List;

public class ScheduleDto {

    private int dayOfWeek;
    private int maxConcurrentBookings;
    private boolean work;
    private List<AvailablePeriodDto> availablePeriodDtoList;

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getMaxConcurrentBookings() {
        return maxConcurrentBookings;
    }

    public void setMaxConcurrentBookings(int maxConcurrentBookings) {
        this.maxConcurrentBookings = maxConcurrentBookings;
    }

    public boolean isWork() {
        return work;
    }

    public void setWork(boolean work) {
        this.work = work;
    }

    public List<AvailablePeriodDto> getAvailablePeriodDtoList() {
        return availablePeriodDtoList;
    }

    public void setAvailablePeriodDtoList(List<AvailablePeriodDto> availablePeriodDtoList) {
        this.availablePeriodDtoList = availablePeriodDtoList;
    }
}
