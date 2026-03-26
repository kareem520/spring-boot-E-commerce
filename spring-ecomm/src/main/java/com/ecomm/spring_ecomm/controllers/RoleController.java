package com.ecomm.spring_ecomm.controllers;

import com.ecomm.spring_ecomm.DTOS.RoleDTO;
import com.ecomm.spring_ecomm.Repositories.RoleRepository;
import com.ecomm.spring_ecomm.models.Role;
import com.ecomm.spring_ecomm.services.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "roles", description = "Role API")
public class RoleController {


    @Autowired
    RoleService roleService;

    @PostMapping("/admin/roles/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> addRole(@RequestParam  String roleName, @RequestParam String customerEmail){
        roleService.addRoleToCustomer(roleName,customerEmail);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getRoles(){
        return ResponseEntity.ok().body(roleService.getRoles());
    }
}
