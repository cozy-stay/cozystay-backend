package rw.cozy.cozybackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rw.cozy.cozybackend.enums.EVisibility;
import rw.cozy.cozybackend.model.User;
import rw.cozy.cozybackend.model.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAllByVisibility(EVisibility visibility);
    Page<User> findAllByVisibility(EVisibility visibility , Pageable pageable);
    List<User> findAllByRolesContainsAndVisibility(UserRole role, EVisibility visibility);
    Page<User> findAllByRolesContainsAndVisibility(UserRole role, EVisibility visibility , Pageable pageable);
    List<User> findAllByTenantId(UUID tenantId);
    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE u.tenantId = :tenantId AND r.name NOT IN :roleNames")
    List<User> findAllByTenantIdAndRolesNotContaining(@Param("tenantId") UUID tenantId, @Param("roleNames") Set<String> roleNames);

}
