package rw.cozy.cozybackend.services;

import rw.cozy.cozybackend.dtos.request.LoginDTO;
import rw.cozy.cozybackend.dtos.request.ResetPasswordDTO;
import rw.cozy.cozybackend.dtos.response.LoginResponse;
import rw.cozy.cozybackend.dtos.response.ProfileResponseDTO;
import rw.cozy.cozybackend.model.User;

public interface IAuthService {
    LoginResponse login(LoginDTO dto);
    boolean verifyAccount(String email, String code);
    boolean verifyResetCode(String email, String code);
    User resendVerificationCode(String email);
    User resetPassword(ResetPasswordDTO dto);
    User initiatePasswordReset(String email);
    // other methods
    ProfileResponseDTO getUserProfile();
    User resendInvitationCode(String email);
}
