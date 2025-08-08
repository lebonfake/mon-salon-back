package com.monsalon.monSalonBackend.controllers;

import com.monsalon.monSalonBackend.Services.AuthService;
import com.monsalon.monSalonBackend.Services.ReservationsService;
import com.monsalon.monSalonBackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    @Autowired
    private ReservationsService reservationsService;
    @Autowired
    private AuthService authService;


    @GetMapping("/formOptionsSalon")
    public ResponseEntity<?> getFormOptionsSalon(){
        System.out.println("wslat chi haja");
       try{
           return ResponseEntity.ok(reservationsService.getReservationOptionsSalon());
       }
       catch(RuntimeException e){
           return ResponseEntity.badRequest().body(e);
       }

    }
}
