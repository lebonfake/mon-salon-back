package com.monsalon.monSalonBackend.controllers;


import com.monsalon.monSalonBackend.Dto.Response;
import com.monsalon.monSalonBackend.Dto.planningCreation.RequestPlanningDto;
import com.monsalon.monSalonBackend.Dto.planningRead.WholeScheduleDto;
import com.monsalon.monSalonBackend.Services.WholeScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plannings")
public class WholeScheduleController {
    @Autowired
    private WholeScheduleService wholeScheduleService;

    @PostMapping()
    public ResponseEntity<WholeScheduleDto> createPlanning(@RequestBody RequestPlanningDto dto){
        WholeScheduleDto created = wholeScheduleService.createPlanning(dto);
        if(created != null)
            return ResponseEntity.ok(created);
        else
            return ResponseEntity.badRequest().body(new WholeScheduleDto());
    }

   @GetMapping()
    public  ResponseEntity<List<WholeScheduleDto>> getPlannings(){
        return ResponseEntity.ok(wholeScheduleService.getPlannings());
   }

   @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePlanning(@PathVariable Long id){
        return ResponseEntity.ok(wholeScheduleService.deletePlanning(id));
   }

   @PatchMapping("/enableDisable/{id}")
    public  ResponseEntity<Response> disableEnable(@PathVariable  Long id){
        wholeScheduleService.ActiverDesactiver(id);
        return ResponseEntity.ok().body(new Response("Le statut du planning est bien changé ","success"));
   }

   @PutMapping("/{id}")
   public ResponseEntity<WholeScheduleDto> editPlanning(@PathVariable Long id , @RequestBody RequestPlanningDto dto){
       WholeScheduleDto created = wholeScheduleService.editPlanning(dto,id);
       if(created != null)
           return ResponseEntity.ok(created);
       else
           return ResponseEntity.badRequest().body(new WholeScheduleDto());
   }
}
