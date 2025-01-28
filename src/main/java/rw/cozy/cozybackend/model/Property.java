package rw.cozy.cozybackend.model;

import jakarta.persistence.*;
import lombok.Data;
import rw.cozy.cozybackend.utils.TimestampAudit;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(indexes = {
        @Index(name = "idx_property_name", columnList = "name"),
        @Index(name = "idx_property_is_active", columnList = "isActive"),
        @Index(name = "idx_property_check_in", columnList = "checkIn"),
})
public class Property extends TimestampAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String description;
    private String checkIn;
    private String checkOut;
    private boolean isActive = true;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private Set<PropertyRoom> propertyRooms = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "property_amenities",
        joinColumns = @JoinColumn(name = "property_id"),
        inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<Amenity> amenities = new HashSet<>();
}
