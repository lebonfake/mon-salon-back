package com.monsalon.monSalonBackend.Dto;

public class Response {
    private String Message;
    private String Status;

    public Response() {

    }

    public Response(String message, String status) {
        Message = message;
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
