package rw.cozy.cozybackend.model;

import jakarta.persistence.*;
import lombok.Data;
import rw.cozy.cozybackend.enums.ERoomType;

import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private ERoomType roomType;

    @OneToMany(mappedBy = "room")
    private Set<PropertyRoom> propertyRooms;
}
