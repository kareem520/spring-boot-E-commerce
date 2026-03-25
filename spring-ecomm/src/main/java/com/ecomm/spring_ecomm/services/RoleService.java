package com.ecomm.spring_ecomm.services;

import com.ecomm.spring_ecomm.models.Role;

public interface RoleService {


    void addRoleToCustomer(String roleName, String customerEmail);
}
