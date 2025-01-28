package rw.cozy.cozybackend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rw.cozy.cozybackend.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {
    private String username;
    private String password;
    private List<GrantedAuthority> authorities=new ArrayList<>();

    public UserPrincipal(User userInfo) {
        this.username = userInfo.getUsername();
        this.password = userInfo.getPassword();
        userInfo.getRoles().forEach(role -> {
            UserAuthority userAuthority = new UserAuthority(userInfo.getId() ,  role.getName());
            authorities.add(userAuthority);
        });
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement your logic if you need this
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement your logic if you need this
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement your logic if you need this
    }

    @Override
    public boolean isEnabled() {
        return true; // Implement your logic if you need this
    }
}
