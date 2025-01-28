package rw.cozy.cozybackend.services;





import rw.cozy.cozybackend.dtos.request.CreateRoleDTO;
import rw.cozy.cozybackend.model.UserRole;

import java.util.List;
import java.util.UUID;

public interface IRoleService {
    // CRUD operations on Roles
    List<UserRole> getAllRoles();
    UserRole getRoleById(UUID id);
    UserRole createRole(CreateRoleDTO createRoleDTO);
    UserRole updateRole(UUID id, CreateRoleDTO createRoleDTO);
    void deleteRole(UUID id);
}
