package com.monsalon.monSalonBackend.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Schedule extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int dayOfWeek;
    private int maxConcurrentBookings;
    private boolean work;
    @ManyToOne
    @JoinColumn(name="wholeSchedule_id")
    private WholeSchedule wholeSchedule;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvailabalePeriod> availabalePeriods;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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


    public List<AvailabalePeriod> getAvailabalePeriods() {
        return availabalePeriods;
    }

    public void setAvailabalePeriods(List<AvailabalePeriod> availabalePeriods) {
        this.availabalePeriods = availabalePeriods;
    }

    public WholeSchedule getWholeSchedule() {
        return wholeSchedule;
    }

    public void setWholeSchedule(WholeSchedule wholeSchedule) {
        this.wholeSchedule = wholeSchedule;
    }
}
