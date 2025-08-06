package com.monsalon.monSalonBackend.controllers;

import com.monsalon.monSalonBackend.Dto.LoginRequest;
import com.monsalon.monSalonBackend.Dto.LoginResponse;
import com.monsalon.monSalonBackend.Dto.Response;
import com.monsalon.monSalonBackend.Dto.SignupRequest;
import com.monsalon.monSalonBackend.Security.JwtUtil;
import com.monsalon.monSalonBackend.Services.AuthService;
import com.monsalon.monSalonBackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    @Autowired

    private AuthService  authService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println(loginRequest.getPhoneNumber());
            System.out.println(loginRequest.getPassword());
            var authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getPhoneNumber(), loginRequest.getPassword());

            // Authenticate the user
            authenticationManager.authenticate(authenticationToken);

            // If successful, generate JWT token
            String token = jwtUtil.generateToken(loginRequest.getPhoneNumber());

            return ResponseEntity.ok(new LoginResponse(token));

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body(new Response("401","Invalid phone number or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody SignupRequest user){
        User newUser = authService.registerUser(user);
        return ResponseEntity.ok(newUser);

    }
}
