package com.monsalon.monSalonBackend.mappers;

import com.monsalon.monSalonBackend.Dto.*;
import com.monsalon.monSalonBackend.Dto.planningCreation.AvailablePeriodDto;
import com.monsalon.monSalonBackend.Dto.planningCreation.ScheduleDto;
import com.monsalon.monSalonBackend.Dto.planningRead.RAvailablePeriodDto;
import com.monsalon.monSalonBackend.Dto.planningRead.RScheduleDto;
import com.monsalon.monSalonBackend.Dto.planningRead.WholeScheduleDto;
import com.monsalon.monSalonBackend.Dto.reservationCreateDto.ReservationCreateDto;
import com.monsalon.monSalonBackend.Dto.reservationReadDto.ClientInReservationDto;
import com.monsalon.monSalonBackend.Dto.reservationReadDto.ReservationReadDto;
import com.monsalon.monSalonBackend.Dto.reservationReadDto.ServiceInReservationDto;
import com.monsalon.monSalonBackend.Dto.servicesCreation.ServiceCreateDto;
import com.monsalon.monSalonBackend.Dto.servicesCreation.ServiceReadDto;
// New imports for Reservation DTOs
import com.monsalon.monSalonBackend.models.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Set; // Make sure this is imported

public class Mapper {

    // Prevent instantiation
    private Mapper() {}

    // ------------------ User and Salon Mappers ------------------
    public static User fromSignupDtoToUser(SignupRequest signupRequest){
        User user = new User();
        user.setFullName(signupRequest.getFullName());
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.setPasswordHash(signupRequest.getPassword());
        return user;
    }

    public static Salon fromSignupDtoToSalon(SignupRequest signupRequest){
        Salon salon = new Salon();
        salon.setAdresse(signupRequest.getSalonAdresse());
        salon.setName(signupRequest.getSalonName());
        salon.setNumberPhone(signupRequest.getSalonPhoneNumber());
        return salon;
    }

    public static UserDto fromUserToDto(User user){
        if (user == null) return null;
        UserDto userDto = new UserDto();
        userDto.setFullname(user.getFullName());
        userDto.setRoleId(user.getRole() != null ? user.getRole().getId() : null);

        if (user.getRole() != null && user.getRole().getPermissions() != null) {
            List<String> permissions = user.getRole().getPermissions().stream().map(Permission::getName).collect(Collectors.toList());
            userDto.setPermissions(permissions);
        }

        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setId(user.getId());
        return userDto;
    }

    public static ClientDto fromClientToClientDto(Client client){
        if (client == null) return null;
        ClientDto clientDto = new ClientDto();
        clientDto.setId(client.getId());
        clientDto.setPhone(client.getPhoneNumber());
        clientDto.setName(client.getName());
        return clientDto;
    }

    public static ServiceDto fromServiceToServiceDto(Services service){
        if (service == null) return null;
        ServiceDto serviceDto = new ServiceDto();
        serviceDto.setId(service.getId());
        serviceDto.setName(service.getName());
        serviceDto.setDuration(service.getDurationInMinutes());
        serviceDto.setPrice(service.getPrice());
        return serviceDto;
    }

    public static ReservationFormOptionsSalonDto ReservationFormComposer(List<ClientDto> clientDtos, List<ServiceDto> servicesDtos, List<String> availableTimes){
        ReservationFormOptionsSalonDto reservationDtos = new ReservationFormOptionsSalonDto();
        reservationDtos.setClients(clientDtos);
        reservationDtos.setServices(servicesDtos);
        reservationDtos.setAvailableTimeSlots(availableTimes);
        return reservationDtos;
    }

    // ****************** Planning Creation DTO to Entity Mappers ******************

    public static AvailabalePeriod FromDtoToAvailablePeriods(AvailablePeriodDto availablePeriodDto){
        if (availablePeriodDto == null) return null;
        AvailabalePeriod availabalePeriod = new AvailabalePeriod();
        availabalePeriod.setStartTime(availablePeriodDto.getStartTime());
        availabalePeriod.setEndTime(availablePeriodDto.getEndTime());
        return availabalePeriod;
    }

    public static Schedule fromDtoToSchedule(ScheduleDto scheduleDto){
        if (scheduleDto == null) return null;
        Schedule schedule = new Schedule();

        List<AvailabalePeriod> availablePeriods = scheduleDto.getAvailablePeriodDtoList() != null ?
                scheduleDto.getAvailablePeriodDtoList()
                        .stream()
                        .map(Mapper::FromDtoToAvailablePeriods)
                        .collect(Collectors.toList()) : List.of();

        availablePeriods.forEach(period -> {
            if (period != null) period.setSchedule(schedule);
        });

        schedule.setWork(scheduleDto.isWork());
        schedule.setAvailabalePeriods(availablePeriods);
        schedule.setMaxConcurrentBookings(scheduleDto.getMaxConcurrentBookings());
        schedule.setDayOfWeek(scheduleDto.getDayOfWeek());

        return schedule;
    }

    // ****************** Planning Read DTO to Entity Mappers ******************
    public static AvailabalePeriod fromRAvailablePeriodDtoToAvailablePeriod(RAvailablePeriodDto rAvailablePeriodDto) {
        if (rAvailablePeriodDto == null) return null;
        AvailabalePeriod availabalePeriod = new AvailabalePeriod();
        availabalePeriod.setId(rAvailablePeriodDto.getId());
        availabalePeriod.setStartTime(rAvailablePeriodDto.getStartTime());
        availabalePeriod.setEndTime(rAvailablePeriodDto.getEndTime());
        return availabalePeriod;
    }

    public static Schedule fromRScheduleDtoToSchedule(RScheduleDto rScheduleDto) {
        if (rScheduleDto == null) return null;
        Schedule schedule = new Schedule();
        schedule.setId(rScheduleDto.getId());
        schedule.setDayOfWeek(rScheduleDto.getDayOfWeek());
        schedule.setMaxConcurrentBookings(rScheduleDto.getMaxConcurrentBookings());
        schedule.setWork(rScheduleDto.isWork());

        List<AvailabalePeriod> availablePeriods = rScheduleDto.getAvailabalePeriods() != null ?
                rScheduleDto.getAvailabalePeriods()
                        .stream()
                        .map(Mapper::fromRAvailablePeriodDtoToAvailablePeriod)
                        .collect(Collectors.toList()) : List.of();

        availablePeriods.forEach(period -> {
            if (period != null) period.setSchedule(schedule);
        });
        schedule.setAvailabalePeriods(availablePeriods);

        return schedule;
    }

    public static WholeSchedule fromWholeScheduleDtoToWholeSchedule(WholeScheduleDto wholeScheduleDto) {
        if (wholeScheduleDto == null) return null;
        WholeSchedule wholeSchedule = new WholeSchedule();
        wholeSchedule.setId(wholeScheduleDto.getId());
        wholeSchedule.setName(wholeScheduleDto.getName());
        wholeSchedule.setCurrentlyUsed(wholeScheduleDto.isCurrentlyUsed());

        List<Schedule> schedules = wholeScheduleDto.getSchedules() != null ?
                wholeScheduleDto.getSchedules()
                        .stream()
                        .map(Mapper::fromRScheduleDtoToSchedule)
                        .collect(Collectors.toList()) : List.of();

        schedules.forEach(schedule -> {
            if (schedule != null) schedule.setWholeSchedule(wholeSchedule);
        });
        wholeSchedule.setSchedules(schedules);

        return wholeSchedule;
    }


    // ****************** Entity to Read DTO Mappers ******************

    public static RAvailablePeriodDto fromAvailablePeriodToDto(AvailabalePeriod availabalePeriod) {
        if (availabalePeriod == null) return null;
        RAvailablePeriodDto dto = new RAvailablePeriodDto();
        dto.setId(availabalePeriod.getId());
        dto.setStartTime(availabalePeriod.getStartTime());
        dto.setEndTime(availabalePeriod.getEndTime());
        return dto;
    }

    public static RScheduleDto fromScheduleToDto(Schedule schedule) {
        if (schedule == null) return null;
        RScheduleDto dto = new RScheduleDto();
        dto.setId(schedule.getId());
        dto.setDayOfWeek(schedule.getDayOfWeek());
        dto.setMaxConcurrentBookings(schedule.getMaxConcurrentBookings());
        dto.setWork(schedule.isWork());

        if (schedule.getAvailabalePeriods() != null) {
            dto.setAvailabalePeriods(
                    schedule.getAvailabalePeriods().stream()
                            .map(Mapper::fromAvailablePeriodToDto)
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

    public static WholeScheduleDto fromWholeScheduleToDto(WholeSchedule wholeSchedule) {
        if (wholeSchedule == null) return null;
        WholeScheduleDto dto = new WholeScheduleDto();
        dto.setId(wholeSchedule.getId());
        dto.setName(wholeSchedule.getName());
        dto.setCurrentlyUsed(wholeSchedule.isCurrentlyUsed());

        if (wholeSchedule.getSchedules() != null) {
            dto.setSchedules(
                    wholeSchedule.getSchedules().stream()
                            .map(Mapper::fromScheduleToDto)
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

    // ****************** SERVICE MAPPERS (Already Existing) ******************

    public static Services fromServiceCreateDtoToService(ServiceCreateDto dto) {
        if (dto == null) {
            return null;
        }
        Services service = new Services();
        service.setName(dto.getName());
        service.setDurationInMinutes(dto.getDurationInMinutes());
        service.setPrice(dto.getPrice());
        return service;
    }

    public static ServiceReadDto fromServiceToServiceReadDto(Services service) {
        if (service == null) {
            return null;
        }
        ServiceReadDto dto = new ServiceReadDto();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setDurationInMinutes(service.getDurationInMinutes());
        dto.setPrice(service.getPrice());
        return dto;
    }

    // ****************** NEW RESERVATION MAPPERS ******************

    /**
     * Maps a ReservationCreateDto to a Reservation entity.
     * Note: 'Client', 'Salon', and 'Services' entities are NOT mapped here.
     * They should be fetched/created and set in the service layer where business logic resides.
     *
     * @param dto The ReservationCreateDto received from the client.
     * @return A Reservation entity with basic fields populated.
     */
    public static Reservation fromReservationCreateDtoToReservation(ReservationCreateDto dto) {
        if (dto == null) {
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());
        // clientName, clientPhoneNumber, clientId, serviceIds are handled in the service layer
        // date, total, status, salon, client, services will also be set in the service layer
        return reservation;
    }

    /**
     * Maps a Reservation entity to a ReservationReadDto.
     * Includes simplified DTOs for Client and Services.
     *
     * @param reservation The Reservation entity from the database.
     * @return A ReservationReadDto for sending to the client.
     */
    public static ReservationReadDto fromReservationToReadDto(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        ReservationReadDto dto = new ReservationReadDto();
        dto.setId(reservation.getId());
        dto.setDate(reservation.getDate());
        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        dto.setTotal(reservation.getTotal());
        dto.setStatus(reservation.getStatus());

        // Map Client
        if (reservation.getClient() != null) {
            ClientInReservationDto clientDto = new ClientInReservationDto();
            clientDto.setId(reservation.getClient().getId());
            clientDto.setName(reservation.getClient().getName());
            clientDto.setPhoneNumber(reservation.getClient().getPhoneNumber());
            dto.setClient(clientDto);
        }

        // Map Services
        if (reservation.getServices() != null && !reservation.getServices().isEmpty()) {
            Set<ServiceInReservationDto> serviceDtos = reservation.getServices().stream()
                    .map(service -> {
                        ServiceInReservationDto serviceDto = new ServiceInReservationDto();
                        serviceDto.setId(service.getId());
                        serviceDto.setName(service.getName());
                        serviceDto.setDurationInMinutes(service.getDurationInMinutes());
                        serviceDto.setPrice(service.getPrice());
                        return serviceDto;
                    })
                    .collect(Collectors.toSet());
            dto.setServices(serviceDtos);
        }

        // Note: Salon is NOT mapped here as the provided ReservationReadDto does not have a salon field.
        // If it should, you'd need to add SalonInReservationDto to ReservationReadDto and map it.

        return dto;
    }
}