package com.ecomm.spring_ecomm.controllers;

import com.ecomm.spring_ecomm.AuthUtil.AuthUtil;
import com.ecomm.spring_ecomm.DTOS.Auth.AuthenticationRequest;
import com.ecomm.spring_ecomm.DTOS.Auth.AuthenticationResponse;
import com.ecomm.spring_ecomm.DTOS.Auth.RegistrationRequest;
import com.ecomm.spring_ecomm.DTOS.user.UserDTO;
import com.ecomm.spring_ecomm.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.hibernate.annotations.Fetch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {


    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    AuthUtil authUtil;

    @Operation(summary = "Authenticate a user and return a JWT token")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid
            @RequestBody final AuthenticationRequest request) {
        return ResponseEntity.ok(this.authenticationService.login(request));
    }

    @Operation(summary = "Register a new user and return the created user details")
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(
            @Valid
            @RequestBody final RegistrationRequest request) {
        UserDTO userDTO = this.authenticationService.register(request);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }


}