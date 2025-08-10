package com.monsalon.monSalonBackend.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class WholeSchedule extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean currentlyUsed;
    @ManyToOne()
    @JoinColumn(name = "salon_id")
    private Salon salon;
    @OneToMany(mappedBy = "wholeSchedule" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Schedule> schedules;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Salon getSalon() {
        return salon;
    }

    public void setSalon(Salon salon) {
        this.salon = salon;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

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
}
