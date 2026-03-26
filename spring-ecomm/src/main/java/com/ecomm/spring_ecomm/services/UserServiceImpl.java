package com.ecomm.spring_ecomm.services;

import com.ecomm.spring_ecomm.DTOS.user.ChangePasswordRequest;
import com.ecomm.spring_ecomm.DTOS.user.ProfileUpdateRequest;
import com.ecomm.spring_ecomm.DTOS.user.UserDTO;
import com.ecomm.spring_ecomm.Repositories.UserRepository;
import com.ecomm.spring_ecomm.exception.BusinessException;
import com.ecomm.spring_ecomm.exception.ErrorCode;
import com.ecomm.spring_ecomm.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{


    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user =  this.userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userEmail));
        return  user;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user =  this.userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, email));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public void updateProfileInfo(ProfileUpdateRequest request, String userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest request, String userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new BusinessException(ErrorCode.PASSWORDS_DO_NOT_MATCH);
        }
        if (passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())){
                throw new BusinessException(ErrorCode.NEW_PASSWORD_SAME_AS_OLD);
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
        }
        else {
            throw new BusinessException(ErrorCode.CURRENT_PASSWORD_INVALID);
        }
    }

    @Override
    public void deactivateAccount(String userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));
        if (!user.isEnabled())
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_DEACTIVATED);

        user.setEnabled(true);
        userRepository.save(user);

    }

    @Override
    public void reactivateAccount(String userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));
        if (!user.isEnabled())
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_ACTIVATED);
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public void deleteAccount(String userId) {

    }


}
