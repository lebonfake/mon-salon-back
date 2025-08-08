package com.monsalon.monSalonBackend.repositories;


import com.monsalon.monSalonBackend.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    public Optional<Schedule> findBySalonIdAndDayOfWeek(Long salonId , int dayOfWeek);
}
