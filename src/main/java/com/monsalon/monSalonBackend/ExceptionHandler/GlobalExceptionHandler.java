package com.monsalon.monSalonBackend.ExceptionHandler;

import com.monsalon.monSalonBackend.Dto.Response;
import com.monsalon.monSalonBackend.exceptions.CantHaveNoPlanningEnabledException;
import com.monsalon.monSalonBackend.exceptions.PasswordMismatchException;
import com.monsalon.monSalonBackend.exceptions.ResourceNotFoundException;
import com.monsalon.monSalonBackend.exceptions.UserAlreadyExistException;
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
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<?> handleUserAlreadyExists(UserAlreadyExistException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new Response(ex.getMessage(), "USER_ALREADY_EXISTS"));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new Response(ex.getMessage(), HttpStatus.BAD_REQUEST.toString()));
    }



    @ExceptionHandler(CantHaveNoPlanningEnabledException.class)
    public ResponseEntity<?> handleResourceNotFound(CantHaveNoPlanningEnabledException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new Response(ex.getMessage(), HttpStatus.BAD_REQUEST.toString()));
    }
}
