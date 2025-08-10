package com.monsalon.monSalonBackend.Services;


import com.monsalon.monSalonBackend.Dto.planningCreation.AvailablePeriodDto;
import com.monsalon.monSalonBackend.Dto.planningCreation.RequestPlanningDto;
import com.monsalon.monSalonBackend.Dto.planningRead.WholeScheduleDto;
import com.monsalon.monSalonBackend.mappers.Mapper;
import com.monsalon.monSalonBackend.models.AvailabalePeriod;
import com.monsalon.monSalonBackend.models.Salon;
import com.monsalon.monSalonBackend.models.Schedule;
import com.monsalon.monSalonBackend.models.WholeSchedule;
import com.monsalon.monSalonBackend.repositories.WholeScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WholeScheduleService {
    @Autowired
    private WholeScheduleRepository wholeScheduleRepository;
    @Autowired AuthService authService;

    @Transactional
    public WholeScheduleDto createPlanning(RequestPlanningDto dto){
        try {


            Salon salon = authService.getCurrentUser().getSalon();

            WholeSchedule wholeSchedule = new WholeSchedule();
            wholeSchedule.setName(dto.getName());
            wholeSchedule.setCurrentlyUsed(dto.isCurrentlyUsed());
            wholeSchedule.setSalon(salon);
            wholeSchedule.setId(null);

            // 1. Convert DTOs to entities
            List<Schedule> schedules = dto.getScheduleDtoList().stream().map(Mapper::fromDtoToSchedule).toList();

            // 2. Set the back-reference from each Schedule to the new WholeSchedule
            // This is the crucial missing step for the first level of cascading
            schedules.forEach(schedule -> schedule.setWholeSchedule(wholeSchedule));

            // 3. Set the list on the parent entity
            wholeSchedule.setSchedules(schedules);

            // 4. Save the parent entity, which cascades the save to all children and grandchildren
            WholeSchedule saved = wholeScheduleRepository.save(wholeSchedule);
            WholeScheduleDto savedDto = Mapper.fromWholeScheduleToDto(saved);
            if(dto.isCurrentlyUsed()){
                wholeScheduleRepository.setAllCurrentlyUsedToFalseForSalon(salon.getId(), saved.getId());
            }
            return savedDto;
        }catch(RuntimeException e ){
            System.out.println(e.getMessage());
            return null;

        }
    }

    public List<WholeScheduleDto> getPlannings(){
        Optional<List<WholeSchedule>> wholeScheduleList = wholeScheduleRepository.findBySalonId(authService.getCurrentUser().getSalon().getId());
        if(wholeScheduleList.isPresent()) {
            List<WholeScheduleDto>  wholeScheduleDtos= wholeScheduleList.get().stream().map(Mapper::fromWholeScheduleToDto).toList();
            return wholeScheduleDtos;
        }
        return  null;

    }

    @Transactional // Essential for delete operations
    public boolean deletePlanning(Long id) {
        try {
            // Check if the entity exists before attempting to delete
            if (wholeScheduleRepository.existsById(id)) {
                wholeScheduleRepository.deleteById(id);
                // If deleteById completes without throwing an exception, it was successful
                return true;
            } else {
                // Entity with the given ID was not found
                System.out.println("WholeSchedule with ID " + id + " not found.");
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            // This exception is thrown by deleteById if the ID does not exist,
            // but existsById already handles this. It's good to keep this catch
            // for robustness if existsById is ever removed or for other cases.
            System.out.println("WholeSchedule with ID " + id + " not found during deletion: " + e.getMessage());
            return false;
        } catch (DataIntegrityViolationException e) {
            // This exception occurs if there are foreign key constraints preventing deletion.
            // You MUST handle this by either deleting child records first
            // or configuring cascade options if appropriate.
            System.out.println("Cannot delete WholeSchedule with ID " + id + " due to foreign key constraints: " + e.getMessage());
            // You might throw a custom exception here or return a specific error code
            return false;
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            System.out.println("An unexpected error occurred during deletion of WholeSchedule ID " + id + ": " + e.getMessage());
            e.printStackTrace(); // Log the full stack trace for debugging
            return false;
        }
    }

}
