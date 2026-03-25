package com.ecomm.spring_ecomm.services;

import com.ecomm.spring_ecomm.DTOS.RoleDTO;
import com.ecomm.spring_ecomm.models.Role;

import java.util.List;

public interface RoleService {


    void addRoleToCustomer(String roleName, String customerEmail);
    List<RoleDTO> getRoles();
}
