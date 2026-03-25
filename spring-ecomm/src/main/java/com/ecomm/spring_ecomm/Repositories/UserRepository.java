package com.ecomm.spring_ecomm.Repositories;

import com.ecomm.spring_ecomm.models.AppRole;
import com.ecomm.spring_ecomm.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailIgnoreCase(String email);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
        @Query("""
        SELECT COUNT(u) > 0
        FROM User u
        JOIN u.roles r
        WHERE r.name = :role
         """)
    boolean existsByRoleName(AppRole role);
}
