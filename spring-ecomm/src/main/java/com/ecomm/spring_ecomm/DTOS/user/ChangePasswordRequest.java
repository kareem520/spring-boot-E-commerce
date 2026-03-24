package com.ecomm.spring_ecomm.DTOS.user;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest{
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}
