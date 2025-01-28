package rw.cozy.cozybackend.dtos.request;

import lombok.Data;

import java.util.UUID;

@Data
public class PropertyRoomDTO {
    private UUID id;
    private UUID roomId;
    private int capacity;
    private double price;
    private double sizeSqm;
    private int bedCount;
    private boolean isAvailable;
}