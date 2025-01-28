package rw.cozy.cozybackend.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rw.cozy.cozybackend.dtos.request.CreateRoleDTO;
import rw.cozy.cozybackend.exceptions.NotFoundException;
import rw.cozy.cozybackend.model.UserRole;
import rw.cozy.cozybackend.repository.IRoleRepository;
import rw.cozy.cozybackend.services.IRoleService;
import rw.cozy.cozybackend.utils.ExceptionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleServiceImpl implements IRoleService {
    private final IRoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Override
    public List<UserRole> getAllRoles() {
        try {
            return roleRepository.findAll();
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    public UserRole getRoleById(UUID id) {
        try {
            return roleRepository.findById(id).orElseThrow(() -> new NotFoundException("Role with id " + id + " was not found"));
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    public UserRole createRole(CreateRoleDTO createRoleDTO) {
        UserRole role = new UserRole();
        role.setName(createRoleDTO.getRoleName());
        role.setPrivileges(createRoleDTO.getPrivileges());
        try {
            Optional<UserRole> role1= roleRepository.findByName(createRoleDTO.getRoleName());
            if (role1.isEmpty()){
                return roleRepository.save(role);
            }
            else{
                return null;
            }
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    public UserRole updateRole(UUID id, CreateRoleDTO createRoleDTO) {
        try {
            UserRole role = roleRepository.findById(id).orElseThrow(() -> new NotFoundException("Role with id " + id + " was not found"));
            role.setName(createRoleDTO.getRoleName());
            role.setPrivileges(createRoleDTO.getPrivileges());
            return roleRepository.save(role);
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    public void deleteRole(UUID id) {
        try {
            UserRole role = roleRepository.findById(id).orElseThrow(() -> new NotFoundException("Role with id " + id + " was not found"));
            roleRepository.deleteById(id);
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
        }
    }
}
