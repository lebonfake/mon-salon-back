package com.monsalon.monSalonBackend.controllers;


import com.monsalon.monSalonBackend.Dto.Response;
import com.monsalon.monSalonBackend.Services.ChecksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/checks")
public class ChecksController {

    @Autowired
    private ChecksService checksService;

    @GetMapping("/hasPlanning")
    public ResponseEntity<Boolean> hasPlanning(){
        System.out.println(" got to controller");
        boolean hasPlanningConfigured = checksService.hasPlanning();
        if(hasPlanningConfigured){
            System.out.println("salon has planning");
            return ResponseEntity.ok(hasPlanningConfigured);
        }
        System.out.println("salon doesnt have planning");
        return ResponseEntity.ok(hasPlanningConfigured);
    }

    @GetMapping("/hasService")
    public ResponseEntity<Boolean> hasService(){
        System.out.println(" got to controller");
        boolean hasServiceConfigured = checksService.hasService();
        if(hasServiceConfigured){
            System.out.println("salon has service");
            return ResponseEntity.ok(hasServiceConfigured);
        }
        System.out.println("salon doesnt have service");
        return ResponseEntity.ok(hasServiceConfigured);
    }
}
