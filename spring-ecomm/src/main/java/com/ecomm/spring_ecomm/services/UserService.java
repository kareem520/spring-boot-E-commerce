package com.ecomm.spring_ecomm.services;

import com.ecomm.spring_ecomm.DTOS.user.ChangePasswordRequest;
import com.ecomm.spring_ecomm.DTOS.user.ProfileUpdateRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

public interface UserService extends UserDetailsService {

    void updateProfileInfo(ProfileUpdateRequest request, String userId);

    void changePassword(ChangePasswordRequest request, String userId);

    void deactivateAccount(String userId);

    void reactivateAccount(String userId);

    void deleteAccount(String userId);
}
