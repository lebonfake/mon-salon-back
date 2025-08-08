package com.monsalon.monSalonBackend.Services;

import com.monsalon.monSalonBackend.Dto.ClientDto;
import com.monsalon.monSalonBackend.Dto.ReservationFormOptionsSalonDto;
import com.monsalon.monSalonBackend.Dto.ServiceDto;
import com.monsalon.monSalonBackend.mappers.Mapper;
import com.monsalon.monSalonBackend.models.*;
import com.monsalon.monSalonBackend.repositories.ClientRepository;
import com.monsalon.monSalonBackend.repositories.ReservationRepository;
import com.monsalon.monSalonBackend.repositories.ScheduleRepository;
import com.monsalon.monSalonBackend.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationsService {
    @Autowired
    private  ReservationRepository reservationRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private  AuthService authService;

    public ReservationFormOptionsSalonDto getReservationOptionsSalon(){
        System.out.println("service is executing");
        User currentUser = authService.getCurrentUser();
        Long salonId = currentUser.getSalon().getId();
        //get salon clients
        List<Client> clients = clientRepository.findBySalonId(salonId).orElse(new ArrayList<Client>());

        //get salon services
        List<Services> services = serviceRepository.findBySalonId(salonId).orElse(new ArrayList<Services>());

        List<String> availableTimes = new ArrayList<>();

        List<ClientDto> clientDtos = clients.stream().map(Mapper::fromClientToClientDto).toList();
        List<ServiceDto> serviceDtos = services.stream().map(Mapper::fromServiceToServiceDto).toList();

        return Mapper.ReservationFormComposer(clientDtos,serviceDtos,availableTimes);




    }
    public List<String> getAvailableTime(int durationInMinutes, Date date,Long salonId) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek(); // e.g., MONDAY
        int day = dayOfWeek.getValue(); // 1 = Monday, ..., 7 = Sunday

        List<String> availableSlots = new ArrayList<>();

        // Fetch schedule for the salon and this day
        Schedule schedule = scheduleRepository.findBySalonIdAndDayOfWeek(salonId, day)
                .orElse(null);

        if (schedule == null || schedule.getAvailabalePeriods() == null)
            return availableSlots;

        // Fetch all confirmed reservations for this date
        List<Reservation> reservations = reservationRepository.findByStatusAndSalonIdAndDate(salonId, localDate);

        // Go through each available period
        for (AvailabalePeriod period : schedule.getAvailabalePeriods()) {
            LocalTime start = period.getStartTime();
            LocalTime end = period.getEndTime();

            LocalDateTime periodStart = localDate.atTime(start);
            LocalDateTime periodEnd = localDate.atTime(end);

            // Slide a window of durationInMinutes across the work period in 5-minute increments
            LocalDateTime windowStart = periodStart;
            while (!windowStart.plusMinutes(durationInMinutes).isAfter(periodEnd)) {
                LocalDateTime windowEnd = windowStart.plusMinutes(durationInMinutes);

                // Check how many reservations overlap with this time window
                int concurrent = 0;
                for (Reservation res : reservations) {
                    if (res.getStartTime().isBefore(windowEnd) && res.getEndTime().isAfter(windowStart)) {
                        concurrent++;
                    }
                }

                if (concurrent < schedule.getMaxConcurrentBookings()) {
                    availableSlots.add(windowStart.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                }

                windowStart = windowStart.plusMinutes(5); // Slide the window
            }
        }

        return availableSlots;
    }



}
