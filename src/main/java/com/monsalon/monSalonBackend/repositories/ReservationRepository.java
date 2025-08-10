package com.monsalon.monSalonBackend.repositories;


import com.monsalon.monSalonBackend.models.Reservation;
import com.monsalon.monSalonBackend.models.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    public List<Reservation> findByStatusAndSalonIdAndDate(ReservationStatus status, Long salonId, LocalDate date);

    // This is your "default" method
    public default List<Reservation> findByStatusAndSalonIdAndDate(Long salonId, LocalDate date) {
        // You can call the full method with the default value
        return findByStatusAndSalonIdAndDate(ReservationStatus.CONFIRME, salonId, date);
    }}
