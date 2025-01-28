package rw.cozy.cozybackend.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.cozy.cozybackend.dtos.request.AddressDTO;
import rw.cozy.cozybackend.dtos.request.PropertyDTO;
import rw.cozy.cozybackend.dtos.request.PropertyRoomDTO;
import rw.cozy.cozybackend.model.*;
import rw.cozy.cozybackend.repository.*;
import rw.cozy.cozybackend.services.IPropertyService;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PropertyServiceImpl implements IPropertyService {

    private IPropertyRepository propertyRepository;
    private IRoomRepository roomRepository;
    private IAmenityRepository amenityRepository;
    private IPropertyRoomRepository propertyRoomRepository;
    private IAddressRepository addressRepository;

    @Override
    public List<PropertyDTO> getAllProperties() {
        return propertyRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public Property getPropertyById(UUID id) {
        return propertyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Property not found"));
    }

    @Override
    public PropertyDTO createProperty(PropertyDTO propertyDTO) {
        Property property = convertToEntity(propertyDTO);
        property = propertyRepository.save(property);
        return convertToDTO(property);
    }

    @Override
    public PropertyDTO updateProperty(UUID id, PropertyDTO propertyDTO) {
        Property property = convertToEntity(propertyDTO);
        property.setId(id);
        property = propertyRepository.save(property);
        return convertToDTO(property);
    }

    @Override
    public void deleteProperty(UUID id) {
        propertyRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PropertyDTO assignRoomsToProperty(UUID propertyId, List<PropertyRoomDTO> propertyRoomDTOs) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new RuntimeException("Property not found"));

        propertyRoomDTOs.forEach(propertyRoomDTO -> {
            Room room = roomRepository.findById(propertyRoomDTO.getRoomId()).orElseThrow(() -> new EntityNotFoundException("Room not found"));

            PropertyRoom propertyRoom = new PropertyRoom();
            propertyRoom.setRoom(room);
            propertyRoom.setProperty(property);
            propertyRoom.setCapacity(propertyRoomDTO.getCapacity());
            propertyRoom.setPrice(propertyRoomDTO.getPrice());
            propertyRoom.setSizeSqm(propertyRoomDTO.getSizeSqm());
            propertyRoom.setBedCount(propertyRoomDTO.getBedCount());
            propertyRoom.setAvailable(propertyRoomDTO.isAvailable());

//            property.getPropertyRooms().add(propertyRoom);

            propertyRoomRepository.save(propertyRoom);
        });

        return convertToDTO(property);
    }

    @Override
    public PropertyDTO assignAmenitiesToProperty(UUID propertyId, List<UUID> amenityIds) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new RuntimeException("Property not found"));
        List<Amenity> amenities = amenityRepository.findAllById(amenityIds);
        property.getAmenities().addAll(new HashSet<>(amenities));
        property = propertyRepository.save(property);
        return convertToDTO(property);
    }

    @Override
    public PropertyDTO assignAddressToProperty(UUID propertyId, AddressDTO addressDTO) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new RuntimeException("Property not found"));

        Address address = new Address();
        address.setCountry(addressDTO.getCountry());
        address.setCity(addressDTO.getCity());
        address.setStreet(addressDTO.getStreet());
        address.setZipCode(addressDTO.getZipCode());
        address.setLatitude(addressDTO.getLatitude());
        address.setLongitude(addressDTO.getLongitude());

        property.setAddress(address);
        property = propertyRepository.save(property);
        return convertToDTO(property);
    }

    private PropertyDTO convertToDTO(Property property) {
       PropertyDTO propertyDTO = new PropertyDTO();
       propertyDTO.setName(property.getName());
         propertyDTO.setDescription(property.getDescription());
            propertyDTO.setCheckIn(property.getCheckIn());
            propertyDTO.setCheckOut(property.getCheckOut());
            return propertyDTO;
    }

    private Property convertToEntity(PropertyDTO propertyDTO) {
        Property property = new Property();
        property.setName(propertyDTO.getName());
        property.setDescription(propertyDTO.getDescription());
        property.setCheckIn(propertyDTO.getCheckIn());
        property.setCheckOut(propertyDTO.getCheckOut());
        return property;
    }
}
