package com.monsalon.monSalonBackend.mappers;

import com.monsalon.monSalonBackend.Dto.SignupRequest;
import com.monsalon.monSalonBackend.models.Salon;
import com.monsalon.monSalonBackend.models.User;

public class SignupRequestMapper {



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
}
