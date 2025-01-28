package rw.cozy.cozybackend.dtos.request;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class PropertyDTO {
    private String name;
    private String description;
    private String checkIn;
    private String checkOut;
//    private AddressDTO address;
//    private Set<PropertyRoomDTO> propertyRooms;
//    private Set<AmenityDTO> amenities;
}