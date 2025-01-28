package rw.cozy.cozybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rw.cozy.cozybackend.model.UserRole;

import java.util.Optional;
import java.util.UUID;

public interface IRoleRepository extends JpaRepository<UserRole, UUID>{

    Optional<UserRole> findByName(String name);
    
}
