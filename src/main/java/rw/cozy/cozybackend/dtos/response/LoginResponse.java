package rw.cozy.cozybackend.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rw.cozy.cozybackend.model.User;
import rw.cozy.cozybackend.model.UserRole;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {

    public String token;
    public User user;
    public Set<UserRole> roles;

    public LoginResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String rolesToString() {
        String rolesString = "";
        for (UserRole role : roles) {
            rolesString += role.getName() + ", ";
        }
        return rolesString;
    }
    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", user=" + user +
                ", roles=" + roles +
                '}';
    }
}
