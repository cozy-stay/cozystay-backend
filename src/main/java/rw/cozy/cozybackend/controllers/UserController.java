package rw.cozy.cozybackend.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import rw.cozy.cozybackend.dtos.request.*;
import rw.cozy.cozybackend.exceptions.BadRequestException;
import rw.cozy.cozybackend.model.User;
import rw.cozy.cozybackend.payload.ApiResponse;
import rw.cozy.cozybackend.serviceImpl.AuthServiceImpl;
import rw.cozy.cozybackend.services.IUserService;
import rw.cozy.cozybackend.utils.ExceptionUtils;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {
    private final RestTemplate restTemplate;
    private final IUserService userService;
    private final String ENTITY_NAME = "User";

    @Autowired
    public UserController(IUserService userService, RestTemplate restTemplate, AuthServiceImpl authServiceImpl) {
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/sendNotification")
    public ResponseEntity<ApiResponse> sendNotification() {
        try {
            log.info("Request to send notification");
//            if(notificationController.notify("The users were fetchedddd")){
//                log.info("Notification sent successfully");
//            }else{
//                log.error("Notification not sent");
//            }
            return new ResponseEntity<>(new ApiResponse(
                    true,
                    "Successfully fetched all users",
                    null
            ), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while getting all {}s", ENTITY_NAME, e);
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    /**
     * @return ResponseEntity<ApiResponse>
     */
    @GetMapping(value = "/all")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers() {
        try {
            log.info("Request to get all {}s", ENTITY_NAME);
            List<User> users = userService.getAllUsers();
            return new ResponseEntity<>(new ApiResponse(
                    true,
                    "Successfully fetched all users",
                    users
            ), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while getting all {}s", ENTITY_NAME, e);
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    /**
     * @param uuid
     * @return
     */
    @GetMapping("/id/{uuid}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> getUserById( @PathVariable String uuid) {
        try {
            log.info("Request to get {} with id {}", ENTITY_NAME, uuid);
            User user = userService.getUserById(UUID.fromString(uuid));
            return new ResponseEntity<>(new ApiResponse(
                    true,
                    "Successfully fetched user with id " + uuid,
                    user
            ), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while getting {} with id {}", ENTITY_NAME, uuid, e);
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    /**
     * @param userDTO
     * @return
     */

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserDTO userDTO) {
        try {
            log.info("Request to create {} with dto {}", ENTITY_NAME, userDTO);
            User createdUser = userService.createUser(userDTO);
            return new ResponseEntity<>(new ApiResponse(
                    true,
                    "Successfully created user",
                    createdUser
            ), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while creating {} with dto {}", ENTITY_NAME, userDTO, e);
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }
    @PostMapping("/create-admin")
    public ResponseEntity<ApiResponse> createUserAdmin(@RequestBody CreateAdminDTO adminDTO) {
        try {
            log.info("Request to create {} with dto {}", ENTITY_NAME, adminDTO);
            User createdUser = userService.createAdmin(adminDTO);
            return new ResponseEntity<>(new ApiResponse(
                    true,
                    "Successfully created Admin please check your email for verification ",
                    createdUser
            ), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while creating {} with dto {}", ENTITY_NAME, adminDTO, e);
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    /**
     * @param uuid
     * @param userDTO
     * @return
     */
    @PutMapping("/update/{uuid}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable String uuid, @RequestBody UpdateUserDTO userDTO) {
        try {
            log.info("Request to update {} with dto {}", ENTITY_NAME, userDTO);
            User updatedUser = userService.updateUser(UUID.fromString(uuid), userDTO);
            return new ResponseEntity<>(new ApiResponse(
                    true,
                    "Successfully updated user",
                    updatedUser
            ), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while updating {} with dto {}", ENTITY_NAME, userDTO, e);
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    /**
     * @param uuid
     * @return
     */
    @DeleteMapping("/void/{uuid}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> voidUser( @PathVariable String uuid) {
        try {
            log.info("Request to void {} with id {}", ENTITY_NAME, uuid);
            User deletedUser = userService.voidUser(UUID.fromString(uuid));
            return new ResponseEntity<>(new ApiResponse(
                    true,
                    "Successfully voided user",
                    deletedUser
            ), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while voiding {} with id {}", ENTITY_NAME, uuid, e);
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    /**
     * @param uuid
     * @return
     */
    @DeleteMapping("/delete/{uuid}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser( @PathVariable String uuid) {
        try {
            log.info("Request to delete {} with id {}", ENTITY_NAME, uuid);
            User deletedUser = userService.deleteUser(UUID.fromString(uuid));
            return new ResponseEntity<>(new ApiResponse(
                    true,
                    "Successfully deleted user",
                    deletedUser
            ), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while deleting {} with id {}", ENTITY_NAME, uuid, e);
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    // other methods of a user

    @PostMapping("/is-code-valid")
    public ResponseEntity<ApiResponse> isCodeValid(@RequestParam String email, @RequestParam String token) {
        try {
            log.info("Request to check if code is valid for user with email {}", email);
            boolean isCodeValid = userService.isCodeValid(email, token);
            if (isCodeValid) {
                return new ResponseEntity<>(new ApiResponse(
                        true,
                        "Successfully verified user",
                        isCodeValid
                ), HttpStatus.OK);
            } else {
                throw new BadRequestException("Invalid code please request a new invitation");
            }
        } catch (Exception e) {
            log.error("Error occurred while checking if code is valid for user with email {}", email, e);
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }



    // reinvite the user
    @PostMapping("/check-profile-completion")
    public ResponseEntity<ApiResponse> checkProfileCompletion(@RequestParam String email) {
        try {
            log.info("Request to check if profile is completed for user with email {}", email);
            boolean isProfileComplete = userService.checkProfileCompletion(email);
            if(isProfileComplete){
                return new ResponseEntity<>(new ApiResponse(
                        true,
                        "Profile completed reset password if forgotten",
                        isProfileComplete
                ), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ApiResponse(
                        true,
                        "Profile not completed please check your email for verification",
                        isProfileComplete
                ), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error occurred while checking if profile is completed for user with email {}", email, e);
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    // getting users by other parameters

    @GetMapping("/role/{role}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> getUsersByRole( @PathVariable String role) {
        try {
            log.info("Request to get {}s by role {}", ENTITY_NAME, role);
            List<User> users = userService.getUsersByRole(UUID.fromString(role));
            return new ResponseEntity<>(new ApiResponse(
                    true,
                    "Successfully fetched users by role",
                    users
            ), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while getting {}s by role {}", ENTITY_NAME, role, e);
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }
}
