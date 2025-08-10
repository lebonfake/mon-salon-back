package com.monsalon.monSalonBackend.Services;


import com.monsalon.monSalonBackend.models.Services;
import com.monsalon.monSalonBackend.models.User;
import com.monsalon.monSalonBackend.models.WholeSchedule;
import com.monsalon.monSalonBackend.repositories.ServiceRepository;
import com.monsalon.monSalonBackend.repositories.WholeScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class ChecksService {

    @Autowired
    private AuthService authService;

    @Autowired
    private WholeScheduleRepository wholeScheduleRepository;
    @Autowired
    private ServiceRepository serviceRepository;


    public boolean hasPlanning(){
        System.out.println("service as planning executed");
        Long salonId = authService.getCurrentUser().getSalon().getId();
        Optional<List<WholeSchedule>> wholeSchedule = wholeScheduleRepository.findBySalonId(salonId);

        if(wholeSchedule.isPresent() && !wholeSchedule.get().isEmpty()) {
            System.out.println("salon : "+ salonId + " has planning");
            return true;
        }
        System.out.println("salon : "+ salonId + " doesnt have planning");
        return false;

    }

    public boolean hasService(){
        System.out.println("service at hasService executed");
        Long salonId = authService.getCurrentUser().getSalon().getId();
        Optional<List<Services>> service = serviceRepository.findBySalonId(salonId);

        if(service.isPresent() && !service.get().isEmpty()) {
            System.out.println("salon : "+ salonId + " has service");
            return true;
        }
        System.out.println("salon : "+ salonId + " doesnt have service");
        return false;

    }
}
