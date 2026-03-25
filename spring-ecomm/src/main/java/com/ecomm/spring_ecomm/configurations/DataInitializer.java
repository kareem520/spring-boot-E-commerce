package com.ecomm.spring_ecomm.configurations;

import com.ecomm.spring_ecomm.Repositories.RoleRepository;
import com.ecomm.spring_ecomm.Repositories.UserRepository;
import com.ecomm.spring_ecomm.exception.BusinessException;
import com.ecomm.spring_ecomm.exception.ErrorCode;
import com.ecomm.spring_ecomm.models.AppRole;
import com.ecomm.spring_ecomm.models.Role;
import com.ecomm.spring_ecomm.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private  AdminProperties adminProperties;
    @Autowired
    RoleRepository roleRepository;
    @Override
    public void run(String... args) {


        boolean adminExists = userRepository.existsByRoleName(AppRole.ROLE_ADMIN);

        if (!adminExists) {
            User admin = new User();
            admin.setEmail(adminProperties.getEmail());
            admin.setPassword(passwordEncoder.encode(adminProperties.getPassword()));
//            Role role = roleRepository.findByName(AppRole.ROLE_ADMIN)
//                    .orElse(new Role(AppRole.ROLE_ADMIN));
            Role role = new Role();
            role.setName(AppRole.ROLE_ADMIN);
            admin.setFirstName("ADMIN");
            admin.setLastName("ADMIN");
            admin.setPhoneNumber("123456789_SYSTEM_NUMBER");
            if (role.getCreatedBy() == null) role.setCreatedBy("SYSTEM");
            if (role.getLastModifiedBy() == null) role.setLastModifiedBy("SYSTEM");
            admin.addRole(role);
            admin.setEnabled(adminProperties.isEnabled());

            userRepository.save(admin);
        }

    }
}