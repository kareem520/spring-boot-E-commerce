package com.ecomm.spring_ecomm.services;


import com.ecomm.spring_ecomm.Repositories.RoleRepository;
import com.ecomm.spring_ecomm.Repositories.UserRepository;
import com.ecomm.spring_ecomm.exception.BusinessException;
import com.ecomm.spring_ecomm.exception.ErrorCode;
import com.ecomm.spring_ecomm.models.AppRole;
import com.ecomm.spring_ecomm.models.Role;
import com.ecomm.spring_ecomm.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public void addRoleToCustomer(String roleName, String customerEmail) {

        switch (roleName) {
          case "ROLE_ADMIN" ->{
              Role role = roleRepository.findByName(AppRole.ROLE_ADMIN)
                      .orElse(new Role(AppRole.ROLE_ADMIN));
              addRole(role,customerEmail);
          }
          case  "ROLE_SELLER" ->{
              Role role = roleRepository.findByName(AppRole.ROLE_SELLER)
                      .orElse(new Role(AppRole.ROLE_SELLER));
              addRole(role,customerEmail);
          }
          case  "ROLE_USER" ->{
              Role role = roleRepository.findByName(AppRole.ROLE_USER)
                      .orElse(new Role(AppRole.ROLE_USER));
              addRole(role,customerEmail);
          }
          default -> throw new BusinessException(ErrorCode.UNKNOWN_ROLE);
        }
    }

    void addRole(Role role, String customerEmail) {
        User user = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, customerEmail));
        List<Role> roles = user.getRoles();
        for (Role in_role:roles) {
            if (role.getName().name().equals(in_role.getName().name())) {
                return;
            }
        }
        roles.add(role);
    }
}
