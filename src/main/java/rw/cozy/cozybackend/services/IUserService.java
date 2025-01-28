package rw.cozy.cozybackend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rw.cozy.cozybackend.dtos.request.CreateAdminDTO;
import rw.cozy.cozybackend.dtos.request.CreateGroupAdminDTO;
import rw.cozy.cozybackend.dtos.request.CreateUserDTO;
import rw.cozy.cozybackend.dtos.request.UpdateUserDTO;
import rw.cozy.cozybackend.model.User;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    // CRUD OPERATIONS
    List<User> getAllUsers();
    Page<User> getAllUsers(Pageable pageable);
    User getUserById(UUID uuid);

    User getUserByEmail(String email);
    User createUser(CreateUserDTO user);

    User updateUser(UUID uuid, UpdateUserDTO user);
    User voidUser(UUID uuid);
    User deleteUser(UUID uuid);

    // OTHER METHODS

    boolean isUserLoggedIn();
    User getLoggedInUser();

    // ALTENATIVE CREATE USER METHOD
    User createAdmin(CreateAdminDTO createAdminDTO);
    User createGroupADMIN(CreateGroupAdminDTO createAdminDTO);
    User createGroupMember(CreateGroupAdminDTO userDTO);
    boolean isCodeValid(String email, String token);
//    User createInviteUser(InviteUserDTO inviteUserDTO, String token);

    // getting users by other parameters
    List<User> getUsersByRole(UUID role);
//    List<User> getAllUsersByRoleAndGroup(String role, UUID group);
    Page<User> getUsersByRole(UUID role, Pageable pageable);

    boolean checkProfileCompletion(String email);
}
