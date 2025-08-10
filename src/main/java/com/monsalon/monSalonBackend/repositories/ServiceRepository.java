package com.monsalon.monSalonBackend.repositories;


import com.monsalon.monSalonBackend.models.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {

    public Optional<List<Services>> findBySalonId(Long salonId);

    // Find all services belonging to a specific salon


    // Find a single service by its ID and ensure it belongs to a specific salon
    Optional<Services> findByIdAndSalonId(Long serviceId, Long salonId);
   }
