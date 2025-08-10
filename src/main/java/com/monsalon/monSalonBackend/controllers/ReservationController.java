package com.monsalon.monSalonBackend.controllers;

import com.monsalon.monSalonBackend.Dto.reservationCreateDto.ReservationCreateDto;
import com.monsalon.monSalonBackend.Dto.reservationReadDto.ReservationReadDto;
import com.monsalon.monSalonBackend.Services.AuthService;
import com.monsalon.monSalonBackend.Services.ReservationsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    @Autowired
    private ReservationsService reservationsService;
    @Autowired
    private AuthService authService;


    @GetMapping("/formOptionsSalon")
    public ResponseEntity<?> getFormOptionsSalon(){
        try{
           return ResponseEntity.ok(reservationsService.getReservationOptionsSalon());
       }
       catch(RuntimeException e){
           return ResponseEntity.badRequest().body(e);
       }

    }
    @GetMapping("/getAvailableTime")
    public ResponseEntity<?> getAvailableTime(@RequestParam int duration , @RequestParam("date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date date){
        System.out.println("wslna akhoya");
        try{
            System.out.println("wslna try");
            return ResponseEntity.ok(reservationsService.getAvailableTime(duration,date,authService.getCurrentUser().getSalon().getId()));


        }
        catch(RuntimeException e){
            System.out.println("tra mochkil : "+e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    /**
     * Creates a new reservation. The method includes re-checking time slot
     * availability and validating end time based on service durations.
     * POST /api/v1/reservations
     * @param createDto The DTO containing the reservation details.
     * @return ResponseEntity with the created ReservationReadDto and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<ReservationReadDto> createReservation(@Valid @RequestBody ReservationCreateDto createDto) {

        System.out.println(createDto.getServiceIds().toString());
        ReservationReadDto createdReservation = reservationsService.createReservation(createDto);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

    /**
     * Retrieves all reservations for the authenticated salon.
     * GET /api/v1/reservations
     * @return ResponseEntity with a list of ReservationReadDto and HTTP status 200 (OK).
     */
    @GetMapping // Handles GET requests to /api/v1/reservations
    public ResponseEntity<List<ReservationReadDto>> getAllReservations() {
        List<ReservationReadDto> reservations = reservationsService.getAllReservationsForSalon();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/salonId")
    public ResponseEntity<?> getSalonId(){
        Long salonId = authService.getSalonId();
        return ResponseEntity.ok(salonId);
    }

}
