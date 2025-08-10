package com.monsalon.monSalonBackend.controllers;

import com.monsalon.monSalonBackend.Services.AuthService;
import com.monsalon.monSalonBackend.Services.ReservationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;

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
}
