package rw.cozy.cozybackend.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.cozy.cozybackend.dtos.request.LoginDTO;
import rw.cozy.cozybackend.dtos.request.ResetPasswordDTO;
import rw.cozy.cozybackend.dtos.response.LoginResponse;
import rw.cozy.cozybackend.dtos.response.ProfileResponseDTO;
import rw.cozy.cozybackend.enums.EUserStatus;
import rw.cozy.cozybackend.exceptions.BadRequestException;
import rw.cozy.cozybackend.exceptions.ResourceNotFoundException;
import rw.cozy.cozybackend.model.User;
import rw.cozy.cozybackend.repository.IUserRepository;
import rw.cozy.cozybackend.security.CustomUserDetailsService;
import rw.cozy.cozybackend.security.JwtService;
import rw.cozy.cozybackend.security.UserAuthority;
import rw.cozy.cozybackend.security.UserPrincipal;
import rw.cozy.cozybackend.services.IAuthService;
import rw.cozy.cozybackend.services.IUserService;
import rw.cozy.cozybackend.utils.ExceptionUtils;
import rw.cozy.cozybackend.utils.Hash;

import java.util.List;
import java.util.UUID;

@Service
public class AuthServiceImpl implements IAuthService {

    private final IUserRepository userRepository;
    private final CustomUserDetailsService userSecurityDetailsService;
    private final IUserService userService;


    private final JwtService jwtUtils;
    private final MailService mailService;
    @Autowired
    public AuthServiceImpl( IUserRepository userRepository, CustomUserDetailsService userSecurityDetailsService, IUserService userService,  JwtService jwtUtils, MailService mailService) {
        this.userRepository = userRepository;
        this.userSecurityDetailsService = userSecurityDetailsService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.mailService = mailService;
    }

    @Override
    public LoginResponse login(LoginDTO dto) {
        try {
            User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new BadRequestException("User with provided email not found"));
            if(Hash.isTheSame(dto.getPassword() , user.getPassword())){
                if(user.getAccountStatus().equals(EUserStatus.ACTIVE)){
                    UserPrincipal userSecurityDetails = (UserPrincipal) userSecurityDetailsService.loadUserByUsername(dto.getEmail());
                    List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) userSecurityDetails.getAuthorities();
                    if(grantedAuthorities.isEmpty()){
                        throw new BadRequestException("User has no role");
                    }
                    UserAuthority userAuthority = (UserAuthority) grantedAuthorities.get(0);
                    String role = userAuthority.getAuthority();
                    String token = jwtUtils.generateToken( dto.getEmail(),user.getId(),role,user);
                    return new LoginResponse(token , user , user.getRoles());
                }else{
                    if(user.getAccountStatus().equals(EUserStatus.DEACTIVATED)){
                        throw new BadRequestException("Please accept your invitation to activate your account");
                    }else{
                        throw new BadRequestException("Account is not active");
                    }
                }
            }else{
                throw new BadRequestException("Incorrect Email or Password");
            }
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional
    public boolean verifyAccount(String email, String code) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User with provided email not found"));
            if(user.getActivationCode().equals(code)){
                user.setAccountStatus(EUserStatus.ACTIVE);
                String password= UUID.randomUUID().toString();
                user.setPassword(Hash.hashPassword(password));
                userRepository.save(user);
//                mailService.sendWelcomeEmail(user,password);
                return true;
            }else{
                throw new BadRequestException("Incorrect verification code");
            }
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return false;
        }
    }

    @Override
    public boolean verifyResetCode(String email, String code) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User with provided email not found"));
            if(user.getActivationCode().equals(code)){
                user.setAccountStatus(EUserStatus.ACTIVE);
                userRepository.save(user);
                return true;
            }else{
                throw new BadRequestException("Incorrect verification code");
            }
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return false;
        }
    }

    @Override
    @Transactional
    public User resendVerificationCode(String email) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User with provided email not found"));
//            mailService.sendAccountVerificationEmail(user);
            return user;
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    @Transactional
    public User resetPassword(ResetPasswordDTO dto) {
        try {
            User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new BadRequestException("User with provided email not found"));
            user.setPassword(Hash.hashPassword(dto.getNewPassword()));
            return user;
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    public User initiatePasswordReset(String email) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User with provided email not found"));
            mailService.sendResetPasswordMail(user);
            return user;
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    public ProfileResponseDTO getUserProfile() {
        try {
            User user = this.userService.getLoggedInUser();

            if(user.getId()!=null && user.getRoles().size() > 0){
                return new ProfileResponseDTO(user , user.getRoles() );
            }else{
                return new ProfileResponseDTO(user , user.getRoles());
            }
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }

    @Override
    public User resendInvitationCode(String email) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User with provided email not found"));
            mailService.sendInvitationEmail(user);
            return user;
        }catch (Exception e){
            ExceptionUtils.handleServiceExceptions(e);
            return null;
        }
    }


}
