package com.monsalon.monSalonBackend.Services;

import com.monsalon.monSalonBackend.Dto.ClientDto;
import com.monsalon.monSalonBackend.Dto.ReservationFormOptionsSalonDto;
import com.monsalon.monSalonBackend.Dto.ServiceDto;
import com.monsalon.monSalonBackend.Dto.reservationCreateDto.ReservationCreateDto;
import com.monsalon.monSalonBackend.Dto.reservationReadDto.ReservationReadDto;
import com.monsalon.monSalonBackend.exceptions.InvalidReservationDataException;
import com.monsalon.monSalonBackend.exceptions.ResourceNotFoundException;
import com.monsalon.monSalonBackend.mappers.Mapper;
import com.monsalon.monSalonBackend.models.*;
import com.monsalon.monSalonBackend.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationsService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate; // Inject SimpMessagingTemplate

    @Autowired
    private WholeScheduleRepository wholeScheduleRepository; // Inject WholeScheduleRepository
    @Autowired
    private ScheduleRepository scheduleRepository; // Inject ScheduleRepository

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
        int day = dayOfWeek.getValue()-1; // 1 = Monday, ..., 7 = Sunday

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


    /**
     * Creates a new reservation based on the provided DTO.
     * Includes re-validation of time slot availability and total duration check.
     *
     * @param createDto The DTO containing reservation details.
     * @return A ReservationReadDto of the created reservation.
     * @throws InvalidReservationDataException if client information is ambiguous or missing,
     * time slot is no longer available, or end time
     * does not match service durations.
     * @throws ResourceNotFoundException if provided client ID or service IDs do not exist.
     */
    @Transactional
    public ReservationReadDto createReservation(ReservationCreateDto createDto) {
        Salon salon = authService.getCurrentUser().getSalon();
        LocalDate reservationDate = createDto.getStartTime().toLocalDate();

        System.out.println("=== DEBUGGING RESERVATION CREATION ===");
        System.out.println("Salon ID: " + salon.getId());
        System.out.println("Requested Service IDs: " + createDto.getServiceIds());

        // --- 1. Handle Client (Existing or New) ---
        Client client;
        if (createDto.hasClientId()) {
            client = clientRepository.findByIdAndSalonId(createDto.getClientId(), salon.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client", "id", createDto.getClientId().toString()));
        } else if (createDto.hasNewClientInfo()) {
            client = clientRepository.findByPhoneNumberAndSalonId(createDto.getClientPhoneNumber(), salon.getId())
                    .orElseGet(() -> {
                        Client newClient = new Client();
                        newClient.setName(createDto.getClientName());
                        newClient.setPhoneNumber(createDto.getClientPhoneNumber());
                        newClient.setSalon(salon);
                        return clientRepository.save(newClient);
                    });
            if (!client.getName().equals(createDto.getClientName())) {
                client.setName(createDto.getClientName());
                clientRepository.save(client);
            }
        } else {
            throw new InvalidReservationDataException("Client information is missing or ambiguous. Provide either clientId or clientName and clientPhoneNumber.");
        }

        // --- 2. Fetch Services and Calculate Total Duration/Price ---
        // ENHANCED DEBUGGING: Check each service ID individually
        Set<Services> services = new HashSet<>();

        for (Long serviceId : createDto.getServiceIds()) {
            System.out.println("Looking for service ID: " + serviceId + " for salon ID: " + salon.getId());

            Optional<Services> serviceOpt = serviceRepository.findByIdAndSalonId(serviceId, salon.getId());
            if (serviceOpt.isPresent()) {
                Services service = serviceOpt.get();
                System.out.println("Found service: ID=" + service.getId() + ", Name=" + service.getName() + ", SalonID=" + service.getSalon().getId());
                services.add(service);
            } else {
                System.err.println("SERVICE NOT FOUND: ID=" + serviceId + " for salon ID=" + salon.getId());
                // Check if service exists at all
                boolean existsGlobally = serviceRepository.existsById(serviceId);
                System.err.println("Service exists globally: " + existsGlobally);
                if (existsGlobally) {
                    // Service exists but not for this salon
                    Optional<Services> globalService = serviceRepository.findById(serviceId);
                    if (globalService.isPresent()) {
                        System.err.println("Service belongs to salon ID: " + globalService.get().getSalon().getId());
                    }
                }
                throw new ResourceNotFoundException("Service", "id", serviceId.toString());
            }
        }

        System.out.println("Total services found: " + services.size());
        services.forEach(s -> System.out.println("Service in set: ID=" + s.getId() + ", Name=" + s.getName()));

        if (services.isEmpty()) {
            throw new InvalidReservationDataException("At least one valid service must be provided.");
        }

        BigDecimal total = services.stream()
                .map(Services::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalDurationMinutes = services.stream()
                .mapToInt(Services::getDurationInMinutes)
                .sum();

        // --- 3. Re-check Time Slot Availability ---
        LocalTime requestedTime = createDto.getStartTime().toLocalTime();

        boolean isStillAvailable = isTimeSlotAvailable(
                salon.getId(),
                reservationDate,
                requestedTime,
                totalDurationMinutes
        );

        if (!isStillAvailable) {
            throw new InvalidReservationDataException("Le créneau horaire sélectionné n'est plus disponible.");
        }

        // --- 4. Verify End Time against Calculated Duration ---
        LocalDateTime calculatedEndTime = createDto.getStartTime().plusMinutes(totalDurationMinutes);
        if (!createDto.getEndTime().equals(calculatedEndTime)) {
            throw new InvalidReservationDataException(
                    "L'heure de fin fournie ne correspond pas à la durée totale des services. " +
                            "Heure de début: " + createDto.getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) +
                            ", Durée totale des services: " + totalDurationMinutes + " minutes. " +
                            "Heure de fin attendue: " + calculatedEndTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
            );
        }

        // --- 5. Create and Save Reservation Entity ---
        Reservation reservation = new Reservation();
        reservation.setDate(reservationDate);
        reservation.setStartTime(createDto.getStartTime());
        reservation.setEndTime(createDto.getEndTime());
        reservation.setTotal(total);
        reservation.setStatus(ReservationStatus.EN_ATTENTE);
        reservation.setSalon(salon);
        reservation.setClient(client);

        // IMPORTANT: Ensure services are managed entities
        System.out.println("Setting services on reservation...");
        reservation.setServices(new HashSet<>(services)); // Create new HashSet to avoid issues

        // Additional debugging before save
        System.out.println("About to save reservation with " + reservation.getServices().size() + " services");
        reservation.getServices().forEach(s ->
                System.out.println("Service to save: ID=" + s.getId() + ", Salon=" + s.getSalon().getId())
        );

        try {
            Reservation savedReservation = reservationRepository.save(reservation);
            System.out.println("Reservation saved successfully with ID: " + savedReservation.getId());

            // --- 6. Map to Read DTO and Return ---

            // --- 7. Send WebSocket Notification ---
            // Send to a topic specific to the salon so only relevant dashboards receive it.
            // The frontend will subscribe to `/topic/salon/{salonId}/reservations`
            String destination = "/topic/salon/" + salon.getId() + "/reservations";
            messagingTemplate.convertAndSend(destination, Mapper.fromReservationToReadDto(savedReservation));
            System.out.println("Sent reservation notification to WebSocket topic: " + destination);

            return Mapper.fromReservationToReadDto(savedReservation);
        } catch (Exception e) {
            System.err.println("Error saving reservation: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Helper method to check if a specific time slot is available.
     * This logic is extracted from your original getAvailableTime, adapted for a specific slot check.
     *
     * @param salonId The ID of the salon.
     * @param date The LocalDate of the reservation.
     * @param requestedStartTime The specific LocalTime of the requested slot start.
     * @param durationMinutes The duration of the reservation in minutes.
     * @return true if the slot is available, false otherwise.
     */
    private boolean isTimeSlotAvailable(Long salonId, LocalDate date, LocalTime requestedStartTime, int durationMinutes) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int day = dayOfWeek.getValue(); // 1 = Monday, ..., 7 = Sunday for Java DayOfWeek

        // Convert to 0-6 for your schedule's dayOfWeek
        int scheduleDay = day == 7 ? 0 : day; // If Sunday (7), map to 0

        WholeSchedule wholeSchedule = wholeScheduleRepository.findBySalonIdAndCurrentlyUsed(salonId, true).orElse(null);
        if (wholeSchedule == null) {
            System.out.println("Validation Error: WholeSchedule not found for salonId: " + salonId);
            return false;
        }

        Schedule schedule = scheduleRepository.findByWholeScheduleIdAndDayOfWeek(wholeSchedule.getId(), scheduleDay)
                .orElse(null);
        if (schedule == null || !schedule.isWork()) { // Ensure schedule exists and salon is working
            System.out.println("Validation Error: Schedule not found or salon not working for day: " + scheduleDay);
            return false;
        }

        if (schedule.getAvailabalePeriods() == null || schedule.getAvailabalePeriods().isEmpty()) {
            System.out.println("Validation Error: No available periods defined for day: " + scheduleDay);
            return false;
        }

        // Check if requested time falls within any available period
        boolean isInPeriod = false;
        for (AvailabalePeriod period : schedule.getAvailabalePeriods()) {
            if (requestedStartTime.isAfter(period.getStartTime().minusMinutes(1)) &&
                    requestedStartTime.plusMinutes(durationMinutes).isBefore(period.getEndTime().plusMinutes(1))) { // Add/subtract a minute to handle exact boundary cases
                isInPeriod = true;
                break;
            }
        }

        if (!isInPeriod) {
            System.out.println("Validation Error: Requested time " + requestedStartTime + " for duration " + durationMinutes + " does not fall within any available period.");
            return false;
        }

        // Count current concurrent reservations for the *requested specific slot*
        LocalDateTime slotStart = date.atTime(requestedStartTime);
        LocalDateTime slotEnd = slotStart.plusMinutes(durationMinutes);

        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(
                salonId, slotStart, slotEnd
        );
        // Note: You might need a custom query in your ReservationRepository for findOverlappingReservations


        return overlappingReservations.size() < schedule.getMaxConcurrentBookings();
    }

    /**
     * Retrieves all reservations for the currently authenticated salon.
     *
     * @return A list of ReservationReadDto.
     */
    @Transactional
    public List<ReservationReadDto> getAllReservationsForSalon() {
        Long salonId = authService.getCurrentUser().getSalon().getId();
        List<Reservation> reservations = reservationRepository.findBySalonId(salonId);
        return reservations.stream()
                .map(Mapper::fromReservationToReadDto)
                .collect(Collectors.toList());
    }



}
