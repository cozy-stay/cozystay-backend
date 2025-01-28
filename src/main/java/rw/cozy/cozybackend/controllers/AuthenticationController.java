package rw.cozy.cozybackend.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.cozy.cozybackend.dtos.request.LoginDTO;
import rw.cozy.cozybackend.dtos.request.ResetPasswordDTO;
import rw.cozy.cozybackend.dtos.response.LoginResponse;
import rw.cozy.cozybackend.dtos.response.ProfileResponseDTO;
import rw.cozy.cozybackend.exceptions.BadRequestException;
import rw.cozy.cozybackend.model.User;
import rw.cozy.cozybackend.payload.ApiResponse;
import rw.cozy.cozybackend.services.IAuthService;
import rw.cozy.cozybackend.utils.ExceptionUtils;

import java.net.URI;


@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthenticationController {
    private final IAuthService authenticationService;
    private final String ENTITY_NAME = "Authentication";

    @Autowired
    public AuthenticationController(IAuthService  authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginDTO loginDTO) {
        try {
            log.info("Request to login user {}", loginDTO.getEmail());
            LoginResponse loginResponse = authenticationService.login(loginDTO);

            return new ResponseEntity<>(new ApiResponse(
                    true,
                    "Login successful",
                    loginResponse
            ), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while logging in user {}", loginDTO.getEmail(), e);
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    @GetMapping("/verify-account")
    public ResponseEntity<Void> verifyAccount(@RequestParam String email, @RequestParam String code) {
        try {
            boolean verificationStatus = authenticationService.verifyAccount(email, code);
            log.info("Request to verify account {}", email);

            if (verificationStatus) {
                // Redirect to your frontend success page
                URI redirectUri = URI.create("http://128.140.67.37:3000/auth/email_verified");
                return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
            } else {
                // Redirect to your frontend failure page
                URI redirectUri = URI.create("http://128.140.67.37:3000/auth/email_failed");
                return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
            }
        } catch (Exception e) {
            log.error("Error occurred while verifying account {}", email, e);
            throw new BadRequestException(e.getMessage());
        }
    }


    @PostMapping("/verify-reset-code")
    public ResponseEntity<ApiResponse> verifyResetCode(@RequestParam String email, @RequestParam String code) {
        try {
            log.info("Request to verify reset code {}", email);
            // Call the verify reset code service method
            boolean verificationStatus = authenticationService.verifyResetCode(email, code);

            // Return a response based on the verification status
            if (verificationStatus) {
                return new ResponseEntity<>(new ApiResponse(
                        true,
                        "Reset code verification successful",
                        null
                ), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(
                        false,
                        "Reset code verification failed",
                        null
                ), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Error occurred while verifying reset code {}", email, e);
            // Handle exceptions and return an appropriate response
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    // Add other methods for reset password, resend verification code, reset password, etc.
    // Example:
    @PostMapping("/resend-verification-code")
    public ResponseEntity<ApiResponse> resendVerificationCode(@RequestParam String email) {
        try {
            log.info("Request to resend verification code {}", email);
            // Call the resend verification code service method
            User user = authenticationService.resendVerificationCode(email);

            // Return a response based on the user
            if (user != null) {
                return new ResponseEntity<>(new ApiResponse(
                        true,
                        "Verification code resent successfully",
                        null
                ), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(
                        false,
                        "Resend verification code failed",
                        null
                ), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Error occurred while resending verification code {}", email, e);
            // Handle exceptions and return an appropriate response
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    @PostMapping("/initiate-reset-password")
    public ResponseEntity<ApiResponse> initiateResetPassword(@RequestParam String email) {
        try {
            log.info("Request to initiate reset password {}", email);
            // Call the initiate reset password service method
            User user = authenticationService.initiatePasswordReset(email);

            // Return a response based on the user
            if (user != null) {
                return new ResponseEntity<>(new ApiResponse(
                        true,
                        "Password reset initiated successfully",
                        null
                ), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(
                        false,
                        "Password reset initiation failed",
                        null
                ), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Error occurred while initiating reset password {}", email, e);
            // Handle exceptions and return an appropriate response
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            log.info("Request to reset password {}", resetPasswordDTO.getEmail());
            // Call the reset password service method
            User user = authenticationService.resetPassword(resetPasswordDTO);

            // Return a response based on the user
            if (user != null) {
                return new ResponseEntity<>(new ApiResponse(
                        true,
                        "Password reset successfully",
                        null
                ), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(
                        false,
                        "Password reset failed",
                        null
                ), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Error occurred while resetting password {}", resetPasswordDTO.getEmail(), e);
            // Handle exceptions and return an appropriate response
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    // Add other methods for reset password, resend verification code, reset password, etc.
    // Example:
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getUserProfile() {
        try {
            log.info("Request to get user profile");
            // Call the get user profile service method
            ProfileResponseDTO profileResponseDTO = authenticationService.getUserProfile();

            // Return a response based on the user
            return new ResponseEntity<>(new ApiResponse(
                    true,
                    "User profile retrieved successfully",
                    profileResponseDTO
            ), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while getting user profile", e);
            // Handle exceptions and return an appropriate response
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }


    // resend invitation code
    @PostMapping("/resend-invitation-code")
    public ResponseEntity<ApiResponse> resendInvitationCode(@RequestParam String email) {
        try {
            log.info("Request to resend invitation code {}", email);
            // Call the resend verification code service method
            User user = authenticationService.resendInvitationCode(email);

            // Return a response based on the user
            if (user != null) {
                return new ResponseEntity<>(new ApiResponse(
                        true,
                        "Invitation code resent successfully",
                        null
                ), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(
                        false,
                        "Resend invitation code failed",
                        null
                ), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Error occurred while resending invitation code {}", email, e);
            // Handle exceptions and return an appropriate response
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

}
