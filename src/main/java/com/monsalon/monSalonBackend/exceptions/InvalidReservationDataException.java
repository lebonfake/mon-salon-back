package com.monsalon.monSalonBackend.exceptions;

public class InvalidReservationDataException extends RuntimeException{
    public  InvalidReservationDataException(String msg){
        super(msg);
    }
}
