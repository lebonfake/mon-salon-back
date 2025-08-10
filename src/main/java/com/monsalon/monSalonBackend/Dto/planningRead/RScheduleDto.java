package com.monsalon.monSalonBackend.Dto.planningRead;

import java.util.List;

public class RScheduleDto {
    private Long id;
    private int dayOfWeek;
    private int maxConcurrentBookings;
    private boolean work;
    private List<RAvailablePeriodDto> availabalePeriods;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(int dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public int getMaxConcurrentBookings() { return maxConcurrentBookings; }
    public void setMaxConcurrentBookings(int maxConcurrentBookings) { this.maxConcurrentBookings = maxConcurrentBookings; }
    public boolean isWork() { return work; }
    public void setWork(boolean work) { this.work = work; }
    public List<RAvailablePeriodDto> getAvailabalePeriods() { return availabalePeriods; }
    public void setAvailabalePeriods(List<RAvailablePeriodDto> availabalePeriods) { this.availabalePeriods = availabalePeriods; }
}