package com.monsalon.monSalonBackend.repositories;

import com.monsalon.monSalonBackend.models.AvailabalePeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailablePeriodRepository extends JpaRepository<AvailabalePeriod,Long> {
}

