package com.monsalon.monSalonBackend.repositories;


import com.monsalon.monSalonBackend.models.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {

    public Optional<List<Services>> findBySalonId(Long salonId);
}
