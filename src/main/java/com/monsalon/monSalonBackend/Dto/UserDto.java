package com.monsalon.monSalonBackend.Dto;

import com.monsalon.monSalonBackend.models.Permission;

import java.util.List;

public class UserDto {

    private long id;
    private String fullname;
    private String phoneNumber;

    private Long roleId;
    private List<String> permissions;
}
