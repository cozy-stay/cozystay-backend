package rw.cozy.cozybackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import rw.cozy.cozybackend.enums.EUserStatus;
import rw.cozy.cozybackend.enums.EVisibility;
import rw.cozy.cozybackend.utils.ExpirationTokenUtils;
import rw.cozy.cozybackend.utils.Utility;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "email" , nullable = false)//    @Email
    private String email;

    @Column(name = "password")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "Password must be strong")
    private String password;

    @Column(name = "username", nullable = false)
    private String username;

    private String firstName;

    private String lastName;


    @Column(name = "activation_code", nullable = false)
    private String activationCode = Utility.randomUUID(6, 0, 'N');

    private String expirationToken = ExpirationTokenUtils.generateToken();

    @Column(name = "status", nullable = false)
    private EUserStatus accountStatus = EUserStatus.ACTIVE;

    @Column(name = "visibilty", nullable = false)
    @JsonIgnore
    private EVisibility visibility = EVisibility.VISIBLE;

    @Column(name = "profile_picture", nullable = true)
    private String profilePicture = null;

    //    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles_mapping",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserRole> roles = new HashSet<>();
    private UUID groupId;
    private UUID memberId;

    @Lazy
    @JsonIgnore
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.addAll(role.getAuthorities()));
        return authorities;
    }

}