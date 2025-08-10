package com.monsalon.monSalonBackend.repositories;

import com.monsalon.monSalonBackend.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long > {
    public Optional<List<Client>> findBySalonId(Long salonId);

    public Optional<Client> findByIdAndSalonId(Long id ,Long salonId);

    public Optional<Client> findByPhoneNumberAndSalonId(String phoneNumber,Long salonId);
}
