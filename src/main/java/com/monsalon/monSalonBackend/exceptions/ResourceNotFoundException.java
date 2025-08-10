package com.monsalon.monSalonBackend.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String resourceName,String resourceColumn , String value) {

        super(resourceName + "n'existe pas avec "+ resourceColumn+" = "+ value);
    }
}
