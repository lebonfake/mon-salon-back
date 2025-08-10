package com.monsalon.monSalonBackend.Services;

import com.monsalon.monSalonBackend.Dto.SignupRequest;
import com.monsalon.monSalonBackend.exceptions.PasswordMismatchException;
import com.monsalon.monSalonBackend.mappers.Mapper;
import com.monsalon.monSalonBackend.models.Role;
import com.monsalon.monSalonBackend.models.Salon;
import com.monsalon.monSalonBackend.models.User;
import com.monsalon.monSalonBackend.repositories.RoleRepository;
import com.monsalon.monSalonBackend.repositories.SalonRepository;
import com.monsalon.monSalonBackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.monsalon.monSalonBackend.exceptions.UserAlreadyExistException;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(SignupRequest signupRequest) {

        User user = Mapper.fromSignupDtoToUser(signupRequest);
        Salon salon = Mapper.fromSignupDtoToSalon(signupRequest);
        System.out.println(" password : " + signupRequest.getPassword());
        System.out.println(" password : " + signupRequest.getConfirmPassword());

        Optional<User> existingUser = userRepository.findByPhoneNumber(user.getPhoneNumber());


        if (signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            user.setPasswordHash(passwordEncoder.encode(signupRequest.getPassword()));
        } else {
            throw new PasswordMismatchException("Le mot de passe et la confirmation ne correspondent pas.");
        }
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException("Num de tel deja utilisé");
        }


        Role role = roleRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        salonRepository.save(salon);
        user.setSalon(salon);
        user.setRole(role);

        User newUser = userRepository.save(user);

        return newUser;

    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = auth.getName(); // assuming phone number is the username
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Long getSalonId(){
        return getCurrentUser().getSalon().getId();
    }



}
