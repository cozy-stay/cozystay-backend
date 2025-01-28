package rw.cozy.cozybackend.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.cozy.cozybackend.dtos.request.CreateAdminDTO;
import rw.cozy.cozybackend.dtos.request.CreateGroupAdminDTO;
import rw.cozy.cozybackend.dtos.request.CreateUserDTO;
import rw.cozy.cozybackend.dtos.request.UpdateUserDTO;
import rw.cozy.cozybackend.enums.EUserStatus;
import rw.cozy.cozybackend.enums.EVisibility;
import rw.cozy.cozybackend.exceptions.BadRequestException;
import rw.cozy.cozybackend.exceptions.NotFoundException;
import rw.cozy.cozybackend.exceptions.UnAuthorizedException;
import rw.cozy.cozybackend.model.User;
import rw.cozy.cozybackend.model.UserRole;
import rw.cozy.cozybackend.repository.IRoleRepository;
import rw.cozy.cozybackend.repository.IUserRepository;
import rw.cozy.cozybackend.security.UserPrincipal;
import rw.cozy.cozybackend.services.IRoleService;
import rw.cozy.cozybackend.services.IUserService;
import rw.cozy.cozybackend.utils.ExceptionUtils;
import rw.cozy.cozybackend.utils.ExpirationTokenUtils;
import rw.cozy.cozybackend.utils.Hash;
import rw.cozy.cozybackend.utils.ServiceUtils;

import java.util.List;
import java.util.UUID;

@Component
@Service
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    private final IRoleService roleService;
    private final MailService mailService;

    @Value("${invitation.valid.days}")
    private int validityDays;

    @Value("${app.registration.code}")
    private String registrationCode;

    @Autowired
    public UserServiceImpl(IUserRepository userRepository, IRoleRepository roleRepository, IRoleService roleService, MailService mailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.mailService = mailService;

    }

    public  boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !authentication.getPrincipal().equals("anonymousUser");
    }

    public User getLoggedInUser() {
        UserPrincipal userSecurityDetails;
        // Retrieve the currently authenticated user from the SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            userSecurityDetails = (UserPrincipal) authentication.getPrincipal();
            return this.userRepository.findByEmail(userSecurityDetails.getUsername()).orElseThrow(() -> new UnAuthorizedException("You are not authorized! please login"));
        } else {
            throw new UnAuthorizedException("You are not authorized! please login");
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            return userRepository.findAllByVisibility(EVisibility.VISIBLE);
        }catch(Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        try {
            return userRepository.findAllByVisibility(EVisibility.VISIBLE , pageable);
        }catch(Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    public User getUserById(UUID uuid) {
        try {
            User user =  userRepository
                    .findById(uuid)
                    .orElseThrow(() -> new NotFoundException("The user with the given id was not found"));
            if(ServiceUtils.isUserDeleted(user)){
                throw new NotFoundException("The user with the given id was not found");
            }else{
                return user;
            }
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            User user =  userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("The user with the given email was not found"));
            if(ServiceUtils.isUserDeleted(user)){
                throw new NotFoundException("The user with the given email was not found");
            }else{
                return user;
            }
        } catch(Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    public User createUser(CreateUserDTO user) {
        try {
            User userByEmail = userRepository.findByEmail(user.getEmail()).orElse(null);
            if(userByEmail != null){
                throw new BadRequestException("The user with the given email already exists");
            }
            User userEntity = new User();
            userEntity.setEmail(user.getEmail());
            userEntity.setUsername(user.getUsername());
            userEntity.setFirstName(user.getFirstName());
            userEntity.setLastName(user.getLastName());
            userEntity.setPassword(Hash.hashPassword(user.getPassword()));
            UserRole role = roleRepository.findById(user.getRoleId()).orElseThrow(()-> new NotFoundException("The role with the given id was not found"));
            userEntity.getRoles().add(role);
            userEntity.setAccountStatus(EUserStatus.ACTIVE);
//            mailService.sendInvitationEmail(userEntity);
//            mailService.sendAccountVerificationEmail(userEntity);
            userRepository.save(userEntity);
            return userEntity;
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }


    @Override
    @Transactional
    public User updateUser(UUID uuid, UpdateUserDTO user) {
        try {
            User userEntity = userRepository.findById(uuid).orElseThrow(()-> new NotFoundException("The user with the provided id was not found"));
            UserRole role = roleService.getRoleById(user.getRoleId());
            if(ServiceUtils.isUserDeleted(userEntity)){
                throw new NotFoundException("The user with the provided id was not found");
            }
            userEntity.getRoles().add(role);
            userEntity.setFirstName(user.getFirstName());
            userEntity.setLastName(user.getLastName());
            userEntity.setUsername(user.getUsername());
            userEntity.setEmail(user.getEmail());
            return userEntity;
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    public User voidUser(UUID uuid) {
        try {
            User user = userRepository
                    .findById(uuid)
                    .orElseThrow(() -> new NotFoundException("The user with the given id was not found"));
            user.setVisibility(EVisibility.VOIDED);
            userRepository.save(user);
            return user;
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    public User deleteUser(UUID uuid) {
        try {
            User user =  userRepository
                    .findById(uuid)
                    .orElseThrow(() -> new NotFoundException("The user with the given id was not found"));
            userRepository.delete(user);
            return user;
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    public boolean validateUserEntry(User user) {
        if (isNotUnique(user)) {
            String errorMessage = "The user with the email: " + user.getEmail() +
                    " already exists";
            throw new BadRequestException(errorMessage);
        } else {
            return true;
        }
    }
    public boolean isNotUnique(User user) {
        try {
            return this.userRepository.findByEmail(user.getEmail()).isPresent();
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return false;
        }
    }

    @Override
    public User createAdmin(CreateAdminDTO createAdminDTO) {
        try {
            if(registrationCode.equals(createAdminDTO.getRegCode())){
                User userByEmail = userRepository.findByEmail(createAdminDTO.getEmail()).orElse(null);
                if(userByEmail != null){
                    throw new BadRequestException("The user with the given email already exists");
                }

                UserRole role = roleRepository.findByName("ADMIN").orElseThrow(()-> new NotFoundException("The role of and admin was not found"));
                User userEntity = new User();
                userEntity.getRoles().add(role);

                userEntity.setFirstName(createAdminDTO.getFirstName());
                userEntity.setLastName(createAdminDTO.getLastName());
                userEntity.setEmail(createAdminDTO.getEmail());
                userEntity.setUsername(createAdminDTO.getUsername());
                userEntity.setPassword(Hash.hashPassword(createAdminDTO.getPassword()));
                userRepository.save(userEntity);

                mailService.sendAccountVerificationEmail(userEntity);
                return userEntity;
            }else{
                throw new BadRequestException("You are not authorized to create an admin");
            }
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    public User createGroupADMIN(CreateGroupAdminDTO createAdminDTO) {
        try {

                User userByEmail = userRepository.findByEmail(createAdminDTO.getEmail()).orElse(null);
                if(userByEmail != null){
                    throw new BadRequestException("The user with the given email already exists");
                }

                UserRole role = roleRepository.findByName("GROUP_ADMIN").orElseThrow(()-> new NotFoundException("The role of and admin was not found"));
                User userEntity = new User();
                userEntity.getRoles().add(role);


                userEntity.setFirstName(createAdminDTO.getFirstName());
                userEntity.setLastName(createAdminDTO.getLastName());
                userEntity.setEmail(createAdminDTO.getEmail());
                userEntity.setUsername(createAdminDTO.getUsername());
                userEntity.setGroupId(createAdminDTO.getGroupId());
                User saved= userRepository.save(userEntity);

                mailService.sendAccountVerificationEmail(userEntity);
                return saved;

        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }
    @Override
    public User createGroupMember(CreateGroupAdminDTO createAdminDTO) {
        try {

            User userByEmail = userRepository.findByEmail(createAdminDTO.getEmail()).orElse(null);
            if(userByEmail != null){
                throw new BadRequestException("The user with the given email already exists");
            }

            UserRole role = roleRepository.findByName("GROUP_MEMBER").orElseThrow(()-> new NotFoundException("The role of and member was not found"));
            User userEntity = new User();
            userEntity.getRoles().add(role);


            userEntity.setFirstName(createAdminDTO.getFirstName());
            userEntity.setLastName(createAdminDTO.getLastName());
            userEntity.setEmail(createAdminDTO.getEmail());
            userEntity.setUsername(createAdminDTO.getUsername());
            userEntity.setMemberId(createAdminDTO.getMemberId());
            userEntity.setGroupId(createAdminDTO.getGroupId());
            User saved= userRepository.save(userEntity);

            mailService.sendAccountVerificationEmail(userEntity);
            return saved;

        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    @Transactional
    public boolean isCodeValid(String email , String token) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("The user with the given email was not found"));
            String expirationToken = user.getExpirationToken();
            if(expirationToken == null){
                throw new BadRequestException("Invalid invitation please first validated the invitation");
            }
            if(!token.equals(expirationToken)) {
                throw new BadRequestException("Invalid invitation please first validated the invitation");
            }
            boolean isValid = ExpirationTokenUtils.isTokenValid(expirationToken, validityDays);
            if(isValid){
                user.setAccountStatus(EUserStatus.NO_PROFILE);
            }
            return isValid;
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return false;
        }
    }



    // getting users by other parameters implementation

    @Override
    public List<User> getUsersByRole(UUID role) {
        try {
            UserRole role1 = roleRepository.findById(role).orElseThrow(()-> new NotFoundException("The role with the given id was not found"));
            return userRepository.findAllByRolesContainsAndVisibility(role1 , EVisibility.VISIBLE);
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    public Page<User> getUsersByRole(UUID role, Pageable pageable) {
        try {
            UserRole role1 = roleRepository.findById(role).orElseThrow(()-> new NotFoundException("The role with the given id was not found"));
            return userRepository.findAllByRolesContainsAndVisibility(role1 , EVisibility.VISIBLE , pageable);
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    public boolean checkProfileCompletion(String email) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("The user with the given email was not found"));
            if(user.getAccountStatus().equals(EUserStatus.NO_PROFILE ) || user.getAccountStatus().equals(EUserStatus.DEACTIVATED)){
                mailService.sendInvitationEmail(user);
                return false;
            }else{
                return true;
            }
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return false;
        }
    }


}