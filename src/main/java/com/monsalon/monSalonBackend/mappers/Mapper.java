package com.monsalon.monSalonBackend.mappers;

import com.monsalon.monSalonBackend.Dto.*;
import com.monsalon.monSalonBackend.Dto.planningCreation.AvailablePeriodDto;
import com.monsalon.monSalonBackend.Dto.planningCreation.ScheduleDto;
import com.monsalon.monSalonBackend.Dto.planningRead.RAvailablePeriodDto;
import com.monsalon.monSalonBackend.Dto.planningRead.RScheduleDto;
import com.monsalon.monSalonBackend.Dto.planningRead.WholeScheduleDto;
import com.monsalon.monSalonBackend.models.*;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {



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
        UserDto userDto = new UserDto();
        userDto.setFullname(user.getFullName());
        userDto.setRoleId(user.getRole().getId());

        List<String> permissions = user.getRole().getPermissions().stream().map(Permission::getName).collect(Collectors.toList());
        userDto.setPermissions(permissions);

        userDto.setPhoneNumber(user.getPhoneNumber());

        userDto.setId(user.getId());

        return userDto;

    }

    public static ClientDto fromClientToClientDto(Client client){
        ClientDto clientDto = new ClientDto();

        clientDto.setId(client.getId());
        clientDto.setPhone(client.getPhoneNumber());
        clientDto.setName(client.getName());

        return clientDto;
    }

    public static ServiceDto fromServiceToServiceDto(Services service){
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

    //****************** planning mappers ******************
    public static AvailabalePeriod FromDtoToAvailablePeriods(AvailablePeriodDto availablePeriodDto){
        AvailabalePeriod availabalePeriod = new AvailabalePeriod();

        availabalePeriod.setStartTime(availablePeriodDto.getStartTime());
        availabalePeriod.setEndTime(availablePeriodDto.getEndTime());
        return availabalePeriod;

    }

    public  static Schedule  fromDtoToSchedule(ScheduleDto scheduleDto){
        Schedule schedule = new Schedule();

        List<AvailabalePeriod> availablePeriods = scheduleDto.getAvailablePeriodDtoList()
                .stream()
                .map(Mapper::FromDtoToAvailablePeriods)
                .toList();

        // Set the back-reference for each child entity
        // This is the crucial missing step
        availablePeriods.forEach(period -> period.setSchedule(schedule));
        schedule.setWork(scheduleDto.isWork());
        schedule.setAvailabalePeriods(availablePeriods);
        schedule.setMaxConcurrentBookings(scheduleDto.getMaxConcurrentBookings());
        schedule.setDayOfWeek(scheduleDto.getDayOfWeek());

        return schedule;

    }


    public static RAvailablePeriodDto fromAvailablePeriodToDto(AvailabalePeriod availabalePeriod) {
        if (availabalePeriod == null) {
            return null;
        }

        RAvailablePeriodDto dto = new RAvailablePeriodDto();
        dto.setId(availabalePeriod.getId());
        dto.setStartTime(availabalePeriod.getStartTime());
        dto.setEndTime(availabalePeriod.getEndTime());
        return dto;
    }
    //------------------------------------------------------------------------------------------------------------------------------------
    public static RScheduleDto fromScheduleToDto(Schedule schedule) {
        if (schedule == null) {
            return null;
        }

        RScheduleDto dto = new RScheduleDto();
        dto.setId(schedule.getId());
        dto.setDayOfWeek(schedule.getDayOfWeek());
        dto.setMaxConcurrentBookings(schedule.getMaxConcurrentBookings());
        dto.setWork(schedule.isWork());

        // Map the list of available periods
        if (schedule.getAvailabalePeriods() != null) {
            dto.setAvailabalePeriods(
                    schedule.getAvailabalePeriods().stream()
                            .map(Mapper::fromAvailablePeriodToDto)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    //------------------------------------------------------------------------------------------------------------------------------------
    public static WholeScheduleDto fromWholeScheduleToDto(WholeSchedule wholeSchedule) {
        if (wholeSchedule == null) {
            return null;
        }

        WholeScheduleDto dto = new WholeScheduleDto();
        dto.setId(wholeSchedule.getId());
        dto.setName(wholeSchedule.getName());
        dto.setCurrentlyUsed(wholeSchedule.isCurrentlyUsed());

        // Map the Salon

        // Map the list of schedules
        if (wholeSchedule.getSchedules() != null) {
            dto.setSchedules(
                    wholeSchedule.getSchedules().stream()
                            .map(Mapper::fromScheduleToDto)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }


}
