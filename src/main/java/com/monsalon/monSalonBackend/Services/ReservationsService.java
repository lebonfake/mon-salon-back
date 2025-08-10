package com.monsalon.monSalonBackend.Services;

import com.monsalon.monSalonBackend.Dto.ClientDto;
import com.monsalon.monSalonBackend.Dto.ReservationFormOptionsSalonDto;
import com.monsalon.monSalonBackend.Dto.ServiceDto;
import com.monsalon.monSalonBackend.mappers.Mapper;
import com.monsalon.monSalonBackend.models.*;
import com.monsalon.monSalonBackend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    WholeScheduleRepository wholeScheduleRepository;
    @Autowired
    private  AuthService authService;

    public ReservationFormOptionsSalonDto getReservationOptionsSalon(){
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
    public List<String> getAvailableTime(int durationInMinutes, Date date, Long salonId) {
        System.out.println("=== DEBUG getAvailableTime ===");
        System.out.println("Input - Duration: " + durationInMinutes + " minutes, Date: " + date + ", SalonId: " + salonId);

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        int day = dayOfWeek.getValue(); // 1 = Monday, ..., 7 = Sunday

        System.out.println("Converted - LocalDate: " + localDate + ", DayOfWeek: " + dayOfWeek + " (value: " + day + ")");

        List<String> availableSlots = new ArrayList<>();

        // Step 1: Check WholeSchedule
        WholeSchedule wholeSchedule = wholeScheduleRepository.findBySalonIdAndCurrentlyUsed(salonId,true).orElse(null);
        if (wholeSchedule == null) {
            System.out.println("ERROR: WholeSchedule not found for salonId: " + salonId);
            return availableSlots;
        }
        System.out.println("✓ WholeSchedule found - ID: " + wholeSchedule.getId());

        // Step 2: Check Schedule for specific day
        Schedule schedule = scheduleRepository.findByWholeScheduleIdAndDayOfWeek(wholeSchedule.getId(), day)
                .orElse(null);
        if (schedule == null) {
            System.out.println("ERROR: Schedule not found for wholeScheduleId: " + wholeSchedule.getId() + " and day: " + day);
            return availableSlots;
        }
        System.out.println("✓ Schedule found - MaxConcurrentBookings: " + schedule.getMaxConcurrentBookings());

        // Step 3: Check available periods
        if (schedule.getAvailabalePeriods() == null || schedule.getAvailabalePeriods().isEmpty()) {
            System.out.println("ERROR: No available periods found in schedule");
            return availableSlots;
        }
        System.out.println("✓ Found " + schedule.getAvailabalePeriods().size() + " available periods");

        // Step 4: Check existing reservations
        List<Reservation> reservations = reservationRepository.findByStatusAndSalonIdAndDate(salonId, localDate);
        System.out.println("✓ Found " + reservations.size() + " existing reservations for " + localDate);

        for (Reservation res : reservations) {
            System.out.println("  - Reservation: " + res.getStartTime() + " to " + res.getEndTime());
        }

        // Step 5: Process each available period
        int totalSlotsAdded = 0;
        for (int i = 0; i < schedule.getAvailabalePeriods().size(); i++) {
            AvailabalePeriod period = schedule.getAvailabalePeriods().get(i);
            LocalTime start = period.getStartTime();
            LocalTime end = period.getEndTime();

            System.out.println("Processing Period " + (i + 1) + ": " + start + " to " + end);

            LocalDateTime periodStart = localDate.atTime(start);
            LocalDateTime periodEnd = localDate.atTime(end);

            // Check if the duration can fit in this period
            if (periodStart.plusMinutes(durationInMinutes).isAfter(periodEnd)) {
                System.out.println("  ⚠ Duration " + durationInMinutes + " minutes doesn't fit in this period (too short)");
                continue;
            }

            int periodSlots = 0;
            LocalDateTime windowStart = periodStart;

            while (!windowStart.plusMinutes(durationInMinutes).isAfter(periodEnd)) {
                LocalDateTime windowEnd = windowStart.plusMinutes(durationInMinutes);

                // Count overlapping reservations
                int concurrent = 0;
                for (Reservation res : reservations) {
                    if (res.getStartTime().isBefore(windowEnd) && res.getEndTime().isAfter(windowStart)) {
                        concurrent++;
                    }
                }

                String timeSlot = windowStart.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                boolean isAvailable = concurrent < schedule.getMaxConcurrentBookings();

                if (isAvailable) {
                    availableSlots.add(timeSlot);
                    periodSlots++;
                    totalSlotsAdded++;
                }

                System.out.println("  Time " + timeSlot + ": concurrent=" + concurrent +
                        "/" + schedule.getMaxConcurrentBookings() +
                        (isAvailable ? " ✓ AVAILABLE" : " ✗ FULL"));

                windowStart = windowStart.plusMinutes(5);
            }

            System.out.println("  Period " + (i + 1) + " contributed " + periodSlots + " available slots");
        }

        System.out.println("=== RESULT: " + totalSlotsAdded + " total available slots ===");
        System.out.println("Available times: " + availableSlots);
        System.out.println("=====================================");

        return availableSlots;
    }



}
