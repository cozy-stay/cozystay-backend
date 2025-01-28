package rw.cozy.cozybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.cozy.cozybackend.model.Room;

import java.util.UUID;

@Repository
public interface IRoomRepository extends JpaRepository<Room, UUID> {
}
