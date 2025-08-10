package com.monsalon.monSalonBackend.repositories;

import com.monsalon.monSalonBackend.models.WholeSchedule;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository

public interface WholeScheduleRepository extends JpaRepository<WholeSchedule,Long> {

    public Optional<List<WholeSchedule>> findBySalonId(Long id);
    public Optional<WholeSchedule>findBySalonIdAndCurrentlyUsed(Long id, Boolean currentlyUsed);


    @Modifying // Indicates that this query modifies data
    @Transactional // Ensures the operation is run within a transaction
    @Query("UPDATE WholeSchedule ws SET ws.currentlyUsed = false WHERE ws.salon.id = :salonId AND ws.id != :id")
    void setAllCurrentlyUsedToFalseForSalon(Long salonId,Long id);

    public Optional<WholeSchedule> findByIdAndSalonId(Long id , Long salonId);
}
