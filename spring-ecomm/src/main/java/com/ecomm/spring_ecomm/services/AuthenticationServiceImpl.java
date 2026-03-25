package com.ecomm.spring_ecomm.services;

import com.ecomm.spring_ecomm.DTOS.Auth.AuthenticationRequest;
import com.ecomm.spring_ecomm.DTOS.Auth.AuthenticationResponse;
import com.ecomm.spring_ecomm.DTOS.Auth.RegistrationRequest;
import com.ecomm.spring_ecomm.DTOS.user.UserDTO;
import com.ecomm.spring_ecomm.Repositories.RoleRepository;
import com.ecomm.spring_ecomm.Repositories.UserRepository;
import com.ecomm.spring_ecomm.exception.BusinessException;
import com.ecomm.spring_ecomm.exception.ErrorCode;
import com.ecomm.spring_ecomm.models.AppRole;
import com.ecomm.spring_ecomm.models.Role;
import com.ecomm.spring_ecomm.models.User;
import com.ecomm.spring_ecomm.security.JwtUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()));


        User user = (User) authentication.getPrincipal();
        String token = jwtUtils.generateTokenFromUsername(user.getUsername());
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        response.setUsername(user.getFirstName() +" "+ user.getLastName());
        response.setEmail(user.getEmail());
        return response;
    }

    @Override
    @Transactional
    public UserDTO register(RegistrationRequest request) {
        checkUserEmail(request.getEmail());
        checkUserPhoneNumber(request.getPhoneNumber());
        checkPasswords(request.getPassword(), request.getConfirmPassword());

        Role role = roleRepository.findByName(AppRole.ROLE_USER)
                .orElse(new Role(AppRole.ROLE_USER));
        role.setCreatedBy("SYSTEM_TEST");
        role.setLastModifiedBy("SYSTEM_TEST");

        User user = modelMapper.map(request, User.class);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        List<Role> roles = new ArrayList<>();
//        roles.add(role);
        user.addRole(role);
        userRepository.save(user);

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return userDTO;


    }

    private void checkUserEmail(String email) {
        boolean emailExist = userRepository.existsByEmail(email);
        if (emailExist)
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
    private void checkUserPhoneNumber(String phoneNumber) {
        boolean phoneNumberExist = userRepository.existsByPhoneNumber(phoneNumber);
        if (phoneNumberExist)
            throw new BusinessException(ErrorCode.PHONE_ALREADY_EXISTS);
    }
    private void checkPasswords(String password, String confirmPassword) {
        if (password==null || !password.equals(confirmPassword))
            throw new BusinessException(ErrorCode.PASSWORDS_DO_NOT_MATCH);
    }
}
