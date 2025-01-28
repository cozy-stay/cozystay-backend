package rw.cozy.cozybackend.model;

import jakarta.persistence.*;
import lombok.*;
import rw.cozy.cozybackend.enums.EPermission;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Privileges {
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_privileges", joinColumns = @JoinColumn(name = "role_id"))
    @Enumerated(EnumType.STRING)
    private List<EPermission> users = new ArrayList<>();

//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "groups_privileges", joinColumns = @JoinColumn(name = "role_id"))
//    @Enumerated(EnumType.STRING)
//    @Column(name = "ekimina_groups", columnDefinition = "VARCHAR(255)")
//    private List<EPermission> groups = new ArrayList<>();
//
//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "members_privileges", joinColumns = @JoinColumn(name = "role_id"))
//    @Enumerated(EnumType.STRING)
//    private List<EPermission> members = new ArrayList<>();
//
//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "loans_privileges", joinColumns = @JoinColumn(name = "role_id"))
//    @Enumerated(EnumType.STRING)
//    private List<EPermission> loans = new ArrayList<>();
//
//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "contributions_privileges", joinColumns = @JoinColumn(name = "role_id"))
//    @Enumerated(EnumType.STRING)
//    private List<EPermission> contributions = new ArrayList<>();
//
//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "withdrawals_privileges", joinColumns = @JoinColumn(name = "role_id"))
//    @Enumerated(EnumType.STRING)
//    @Column(name = "with_draw", columnDefinition = "VARCHAR(255)")
//
//    private List<EPermission> with_draw = new ArrayList<>();

    public Privileges applySamePrivilegesToAll(List<EPermission> privileges){
        setUsers(privileges);
//        setContributions(privileges);
//        setLoans(privileges);
//        setGroups(privileges);
//        setMembers(privileges);
//        setWith_draw(privileges);
        return this;
    }
}
