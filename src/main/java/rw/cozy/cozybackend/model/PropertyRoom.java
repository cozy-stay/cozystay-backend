package rw.cozy.cozybackend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class PropertyRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private int capacity;
    private double price;
    private double sizeSqm;
    private int bedCount;
    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
