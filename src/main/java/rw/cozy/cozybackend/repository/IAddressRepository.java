package rw.cozy.cozybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.cozy.cozybackend.model.Address;

import java.util.UUID;

@Repository
public interface IAddressRepository extends JpaRepository<Address, UUID> {
    Address findByStreet(String street);
}
