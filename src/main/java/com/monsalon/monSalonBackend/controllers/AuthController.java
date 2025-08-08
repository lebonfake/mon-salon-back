package com.monsalon.monSalonBackend.controllers;

import com.monsalon.monSalonBackend.Dto.*;
import com.monsalon.monSalonBackend.Security.JwtUtil;
import com.monsalon.monSalonBackend.Services.AuthService;
import com.monsalon.monSalonBackend.mappers.Mapper;
import com.monsalon.monSalonBackend.models.User;
import com.monsalon.monSalonBackend.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private UserRepository userRepository;


    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
             var authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getPhoneNumber(), loginRequest.getPassword());

            // Authenticate the user
            authenticationManager.authenticate(authenticationToken);

            // If successful, generate JWT token
            String token = jwtUtil.generateToken(loginRequest.getPhoneNumber());
            Cookie cookie = new Cookie("jwt",token);
            cookie.setHttpOnly(true);

            cookie.setSecure(false); // only send over HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60); // 1 day

            response.addCookie(cookie);
            return ResponseEntity.ok(new Response("User logged in succesfully","success"));

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body(new Response("Invalid phone number or password","401"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody SignupRequest user){
        User newUser = authService.registerUser(user);
        return ResponseEntity.ok(newUser);

    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(HttpServletRequest req){
        System.out.println("verify");
        // Extracting the cookie
        Cookie[] cookies= req.getCookies();
        if (cookies == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("No token", "401"));
        }
        //extract token
        String token = null;

        for(Cookie cookie : cookies){
            if("jwt".equals(cookie.getName())){

                token = cookie.getValue();
                break;
            }else{

            }
        }

        // verifying the token

        if(token != null && jwtUtil.validateToken(token)){
           ;
            String phoneNumber  = jwtUtil.extractUsername(token);
            User user = userRepository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            UserDto userDto = Mapper.fromUserToDto(user);
            return ResponseEntity.ok(userDto);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Denied","401"));
        }

        // returning response
    }
}
