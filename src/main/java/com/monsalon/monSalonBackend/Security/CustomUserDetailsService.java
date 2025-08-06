package com.monsalon.monSalonBackend.Security;

import com.monsalon.monSalonBackend.models.User;
import com.monsalon.monSalonBackend.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository repo) {
        this.userRepository = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<SimpleGrantedAuthority> authorities;

        if (user.getRole().getSalon() != null) {
            authorities = user.getRole().getPermissions().stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                    .collect(Collectors.toList());
        } else {
            authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }


        return new org.springframework.security.core.userdetails.User(
                user.getPhoneNumber(),
                user.getPasswordHash(),
                authorities

        );
    }
}
