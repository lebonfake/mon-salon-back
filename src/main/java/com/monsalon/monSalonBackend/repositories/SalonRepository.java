package com.monsalon.monSalonBackend.repositories;

import com.monsalon.monSalonBackend.models.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalonRepository extends JpaRepository<Salon, Long > {
}
