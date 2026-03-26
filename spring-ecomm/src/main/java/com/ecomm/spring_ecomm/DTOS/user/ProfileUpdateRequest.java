package com.ecomm.spring_ecomm.DTOS.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequest {
    @Column(name = "FIRST_NAME", nullable = false)
    @Size(min = 2, max = 50, message = "user first name must be between {min} and {max} characters")
    private String firstName;
    @Size(min = 2, max = 50, message = "user last name must be between {min} and {max} characters")
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;
}
