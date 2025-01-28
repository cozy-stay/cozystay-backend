package rw.cozy.cozybackend.dtos.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rw.cozy.cozybackend.model.User;
import rw.cozy.cozybackend.model.UserRole;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ProfileResponseDTO {
    private User user;
    private Set<UserRole> roles;


    public ProfileResponseDTO(User user, Set<UserRole> roles) {
        this.user = user;
        this.roles = roles;
    }


}
