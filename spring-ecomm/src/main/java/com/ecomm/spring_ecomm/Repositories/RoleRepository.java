package com.ecomm.spring_ecomm.Repositories;

import com.ecomm.spring_ecomm.models.AppRole;
import com.ecomm.spring_ecomm.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(AppRole appRole);
}
