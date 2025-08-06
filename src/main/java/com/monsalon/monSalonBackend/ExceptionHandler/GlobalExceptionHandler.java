package com.monsalon.monSalonBackend.ExceptionHandler;

import com.monsalon.monSalonBackend.Dto.Response;
import com.monsalon.monSalonBackend.exceptions.PasswordMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<Response> handlePasswordMismatch(PasswordMismatchException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
       return new ResponseEntity<>(
                new Response(ex.getMessage(), "ERROR"),
                HttpStatus.ACCEPTED
        );
    }
}
