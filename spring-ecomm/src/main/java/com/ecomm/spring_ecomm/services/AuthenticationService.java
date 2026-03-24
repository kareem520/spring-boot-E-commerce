package com.ecomm.spring_ecomm.services;

import com.ecomm.spring_ecomm.DTOS.Auth.AuthenticationRequest;
import com.ecomm.spring_ecomm.DTOS.Auth.AuthenticationResponse;
import com.ecomm.spring_ecomm.DTOS.Auth.RegistrationRequest;
import com.ecomm.spring_ecomm.DTOS.user.UserDTO;

public interface AuthenticationService {

    AuthenticationResponse login(AuthenticationRequest request);

    UserDTO register(RegistrationRequest request);
}
