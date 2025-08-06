package com.monsalon.monSalonBackend.repositories;

import com.monsalon.monSalonBackend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
