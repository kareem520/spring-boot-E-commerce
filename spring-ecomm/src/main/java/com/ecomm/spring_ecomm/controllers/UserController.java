package com.ecomm.spring_ecomm.controllers;


import com.ecomm.spring_ecomm.DTOS.user.ChangePasswordRequest;
import com.ecomm.spring_ecomm.DTOS.user.ProfileUpdateRequest;
import com.ecomm.spring_ecomm.DTOS.user.UserDTO;
import com.ecomm.spring_ecomm.models.User;
import com.ecomm.spring_ecomm.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@Tag(name = "users", description = "User API")
public class UserController {

    @Autowired
    private UserService userService;

    // ADMIN
    @Operation(summary = "Get a user's details by email (admin only)")
    @GetMapping("/admin/user/email")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    // PUBLIC / AUTHENTICATED USER
    @Operation(summary = "Get the currently authenticated user's profile")
    @GetMapping("/public/me")
    public ResponseEntity<UserDTO> getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((User) authentication.getPrincipal()).getEmail();
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @Operation(summary = "Update the profile information of the currently authenticated user")
    @PatchMapping("/public/me")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateProfile(
            @RequestBody @Valid final ProfileUpdateRequest request,
            final Authentication principal) {
        this.userService.updateProfileInfo(request, getUserId(principal));
    }

    @Operation(summary = "Change the password of the currently authenticated user")
    @PostMapping("/public/me/password")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void changePassword(
            @RequestBody @Valid final ChangePasswordRequest request,
            final Authentication principal) {
        this.userService.changePassword(request, getUserId(principal));
    }

    @Operation(summary = "Deactivate the account of the currently authenticated user")
    @PatchMapping("/public/me/deactivate")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deactivateAccount(final Authentication principal) {
        this.userService.deactivateAccount(getUserId(principal));
    }

    @Operation(summary = "Reactivate the account of the currently authenticated user")
    @PatchMapping("/public/me/reactivate")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void reactivateAccount(final Authentication principal) {
        this.userService.reactivateAccount(getUserId(principal));
    }
    private String getUserId(final Authentication authentication) {
        return ((User) authentication.getPrincipal()).getId();
    }
}
