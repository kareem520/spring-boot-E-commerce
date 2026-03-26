package com.ecomm.spring_ecomm.DTOS.user;


import com.ecomm.spring_ecomm.DTOS.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    List<RoleDTO> roles;

}
