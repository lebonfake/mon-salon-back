package com.monsalon.monSalonBackend.Dto;

public class SignupRequest {
    private String fullName;
    private String phoneNumber;
    private String password;
    private String confirmPassword;
    private String salonName;
    private String salonAdresse;
    private String salonPhoneNumber;


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmpassword) {
        this.confirmPassword = confirmpassword;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public String getSalonAdresse() {
        return salonAdresse;
    }

    public void setSalonAdresse(String salonAdresse) {
        this.salonAdresse = salonAdresse;
    }

    public String getSalonPhoneNumber() {
        return salonPhoneNumber;
    }

    public void setSalonPhoneNumber(String salonPhoneNumber) {
        this.salonPhoneNumber = salonPhoneNumber;
    }
}
