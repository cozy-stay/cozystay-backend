package rw.cozy.cozybackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rw.cozy.cozybackend.model.User;
import rw.cozy.cozybackend.repository.IUserRepository;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IUserRepository repository;

    private final PasswordEncoder encoder;
    @Autowired
    CustomUserDetailsService(IUserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail = repository.findByEmail(username); // Assuming 'email' is used as username
        userDetail.ifPresent(user -> user.setUsername(user.getEmail()));

        // Converting UserInfo to UserDetails
        return userDetail.map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}