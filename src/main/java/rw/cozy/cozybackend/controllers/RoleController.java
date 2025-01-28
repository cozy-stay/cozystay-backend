package rw.cozy.cozybackend.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.cozy.cozybackend.dtos.request.CreateRoleDTO;
import rw.cozy.cozybackend.model.UserRole;
import rw.cozy.cozybackend.payload.ApiResponse;
import rw.cozy.cozybackend.services.IRoleService;
import rw.cozy.cozybackend.utils.ExceptionUtils;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
@Slf4j
public class RoleController {
    private final IRoleService roleService;
    private final String ENTITY_NAME = "Role";
    @Autowired
    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }
    /**
     *
     * @return ResponseEntity<ApiResponse>
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllRoles() {
        try {
            log.info("Request to get all {}s", ENTITY_NAME);
            List<UserRole> roles = roleService.getAllRoles();
            return new ResponseEntity<>(new ApiResponse(
                    true,
                    "Roles fetched successfully",
                    roles
            ), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while getting all {}s", ENTITY_NAME, e);
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    /**
     *
     * @param id
     * @return ResponseEntity<ApiResponse>
     */
    @GetMapping("id/{id}")
    public ResponseEntity<ApiResponse> getRoleById( @PathVariable String id) {
        try {
            log.info("Request to get {} with id {}", ENTITY_NAME, id);
            UserRole role = roleService.getRoleById(UUID.fromString(id));
            return new ResponseEntity<>(new ApiResponse(
                    true,
                    "Role fetched successfully",
                    role
            ), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while getting {} with id {}", ENTITY_NAME, id, e);
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    /**
     *
     * @param roleDTO
     * @return ResponseEntity<ApiResponse>
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createRole(@RequestBody() CreateRoleDTO roleDTO) {
        try {
            log.info("Request to create {} with dto {}", ENTITY_NAME, roleDTO);
            UserRole role = roleService.createRole(roleDTO);
            return new ResponseEntity<>(new ApiResponse(
                    true,
                    "Role created successfully",
                    role
            ), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while creating {} with dto {}", ENTITY_NAME, roleDTO, e);
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    /**
     *
     * @param id
     * @param roleDTO
     * @return ResponseEntity<ApiResponse>
     */

    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse> updateRole( @PathVariable String id, @RequestBody()CreateRoleDTO roleDTO) {
        try {
            log.info("Request to update {} with dto {}", ENTITY_NAME, roleDTO);
            UserRole updatedRole = roleService.updateRole(UUID.fromString(id), roleDTO);
            return new ResponseEntity<>(new ApiResponse(
                    true,
                    "Role updated successfully",
                    updatedRole
            ), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while updating {} with dto {}", ENTITY_NAME, roleDTO, e);
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    /**
     *
     * @param id
     * @return ResponseEntity<ApiResponse>
     */

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteRole( @PathVariable String id) {
        try {
            log.info("Request to delete {} with id {}", ENTITY_NAME, id);
            roleService.deleteRole(UUID.fromString(id));
            return new ResponseEntity<>(new ApiResponse(
                    true,
                    "Role deleted successfully"
            ), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while deleting {} with id {}", ENTITY_NAME, id, e);
                return ExceptionUtils.handleControllerExceptions(e);
        }
    }

}
