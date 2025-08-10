package com.monsalon.monSalonBackend.models;

import jakarta.persistence.*;

import java.util.List;


@Entity
public class Salon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String name;
    private String numberPhone;
    private String email;
    private String adresse;

    @OneToMany(mappedBy = "salon",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> user;
    @OneToMany(mappedBy = "salon",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Role> roles;

    @OneToMany(mappedBy="salon")

    private List<WholeSchedule> wholeSchedule;
    @OneToMany(mappedBy = "salon")
    private List<Client> client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }


    public List<Client> getClient() {
        return client;
    }

    public void setClient(List<Client> client) {
        this.client = client;
    }

    public List<WholeSchedule> getWholeSchedule() {
        return wholeSchedule;
    }

    public void setWholeSchedule(List<WholeSchedule> wholeSchedule) {
        this.wholeSchedule = wholeSchedule;
    }
}
