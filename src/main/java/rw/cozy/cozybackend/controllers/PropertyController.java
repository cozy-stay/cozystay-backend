package rw.cozy.cozybackend.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.cozy.cozybackend.dtos.request.AddressDTO;
import rw.cozy.cozybackend.dtos.request.PropertyDTO;
import rw.cozy.cozybackend.dtos.request.PropertyRoomDTO;
import rw.cozy.cozybackend.model.Property;
import rw.cozy.cozybackend.services.IPropertyService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/property")
@AllArgsConstructor
public class PropertyController {
    private IPropertyService propertyService;

    @GetMapping
    public List<PropertyDTO> getAllProperties() {
        return propertyService.getAllProperties();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable UUID id) {
        return ResponseEntity.ok(propertyService.getPropertyById(id));
    }

    @PostMapping
    public PropertyDTO createProperty(@RequestBody PropertyDTO propertyDTO) {
        return propertyService.createProperty(propertyDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyDTO> updateProperty(@PathVariable UUID id, @RequestBody PropertyDTO propertyDTO) {
        PropertyDTO updatedProperty = propertyService.updateProperty(id, propertyDTO);
        return updatedProperty != null ? ResponseEntity.ok(updatedProperty) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable UUID id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/rooms")
    public ResponseEntity<PropertyDTO> assignRoomsToProperty(@PathVariable UUID id, @RequestBody List<PropertyRoomDTO> propertyRoomDTOs) {
        PropertyDTO updatedProperty = propertyService.assignRoomsToProperty(id, propertyRoomDTOs);
        return ResponseEntity.ok(updatedProperty);
    }

    @PostMapping("/{id}/amenities")
    public ResponseEntity<PropertyDTO> assignAmenitiesToProperty(@PathVariable UUID id, @RequestBody List<UUID> amenityIds) {
        PropertyDTO updatedProperty = propertyService.assignAmenitiesToProperty(id, amenityIds);
        return ResponseEntity.ok(updatedProperty);
    }

    @PostMapping("/{id}/address")
    public ResponseEntity<PropertyDTO> assignAddressToProperty(@PathVariable UUID id, @RequestBody AddressDTO addressDTO) {
        PropertyDTO updatedProperty = propertyService.assignAddressToProperty(id, addressDTO);
        return ResponseEntity.ok(updatedProperty);
    }
}
