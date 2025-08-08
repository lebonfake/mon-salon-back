package com.monsalon.monSalonBackend.mappers;

import com.monsalon.monSalonBackend.Dto.*;
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

        List<String> permissions = user.getRole().getPermissions().stream().map(permission->permission.getName()).collect(Collectors.toList());
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


}
