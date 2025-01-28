package rw.cozy.cozybackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import rw.cozy.cozybackend.enums.EPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_roles")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    @JsonIgnore
    @ManyToMany(mappedBy = "roles" , fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    @Embedded
    private Privileges privileges = new Privileges();

    public UserRole(String name){
        this.name = name;
    }
    public UserRole(){

    }

    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add authorities for each permission in users
        for (EPermission permission : this.privileges.getUsers()) {
            authorities.add(new SimpleGrantedAuthority("USER_" + permission.name()));
        }

//        // Add authorities for each permission in company
//        for (EPermission permission : this.privileges.getContributions()) {
//            authorities.add(new SimpleGrantedAuthority("CONTRIBUTION_" + permission.name()));
//        }
//
//        // Add authorities for each permission in insurance
//        for (EPermission permission : this.privileges.getLoans()) {
//            authorities.add(new SimpleGrantedAuthority("LOAN_" + permission.name()));
//        }
//
//        // Add authorities for each permission in payroll
//        for (EPermission permission : this.privileges.getMembers()) {
//            authorities.add(new SimpleGrantedAuthority("MEMBER_" + permission.name()));
//        }
//
//        // Add authorities for each permission in workers
//        for (EPermission permission : this.privileges.getGroups()) {
//            authorities.add(new SimpleGrantedAuthority("GROUP_" + permission.name()));
//        }
//
//        // Add authorities for each permission in cards
//        for (EPermission permission : this.privileges.getWith_draw()) {
//            authorities.add(new SimpleGrantedAuthority("WITHDRAW_   " + permission.name()));
//        }
        return authorities;
    }
}
