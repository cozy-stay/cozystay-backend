package rw.cozy.cozybackend.services;

import rw.cozy.cozybackend.dtos.request.AddressDTO;
import rw.cozy.cozybackend.dtos.request.PropertyDTO;
import rw.cozy.cozybackend.dtos.request.PropertyRoomDTO;
import rw.cozy.cozybackend.model.Property;

import java.util.List;
import java.util.UUID;

public interface IPropertyService {
    List<PropertyDTO> getAllProperties();
    Property getPropertyById(UUID id);
    PropertyDTO createProperty(PropertyDTO propertyDTO);
    PropertyDTO updateProperty(UUID id, PropertyDTO propertyDTO);
    void deleteProperty(UUID id);

    PropertyDTO assignRoomsToProperty(UUID propertyId, List<PropertyRoomDTO> propertyRoomDTOs);
    PropertyDTO assignAmenitiesToProperty(UUID propertyId, List<UUID> amenityIds);
    PropertyDTO assignAddressToProperty(UUID propertyId, AddressDTO addressDTO);

}
