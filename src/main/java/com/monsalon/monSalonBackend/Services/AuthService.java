package com.monsalon.monSalonBackend.Services;

import com.monsalon.monSalonBackend.Dto.SignupRequest;
import com.monsalon.monSalonBackend.exceptions.PasswordMismatchException;
import com.monsalon.monSalonBackend.mappers.SignupRequestMapper;
import com.monsalon.monSalonBackend.models.Role;
import com.monsalon.monSalonBackend.models.Salon;
import com.monsalon.monSalonBackend.models.User;
import com.monsalon.monSalonBackend.repositories.RoleRepository;
import com.monsalon.monSalonBackend.repositories.SalonRepository;
import com.monsalon.monSalonBackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        User user = SignupRequestMapper.fromSignupDtoToUser(signupRequest);
        Salon salon = SignupRequestMapper.fromSignupDtoToSalon(signupRequest);
        System.out.println(" password : "+signupRequest.getPassword());
        System.out.println(" password : "+signupRequest.getConfirmPassword());
        try {
            if (signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
                user.setPasswordHash(passwordEncoder.encode(signupRequest.getPassword()));
            } else {
                throw new PasswordMismatchException("Le mot de passe et la confirmation ne correspondent pas.");
            }
        } catch (PasswordMismatchException e) {
            System.out.println(e.getMessage());
            throw e;
        }


        Role role = roleRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        salonRepository.save(salon);
        user.setSalon(salon);
        user.setRole(role);

        User newUser = userRepository.save(user);

        return newUser;

    }

}
