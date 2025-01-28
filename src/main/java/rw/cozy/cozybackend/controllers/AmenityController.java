package rw.cozy.cozybackend.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.cozy.cozybackend.dtos.request.AmenityDTO;
import rw.cozy.cozybackend.services.IAmenityService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/amenities")
@AllArgsConstructor
public class AmenityController {
    private IAmenityService amenityService;

    @GetMapping
    public List<AmenityDTO> getAllAmenities() {
        return amenityService.getAllAmenities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmenityDTO> getAmenityById(@PathVariable UUID id) {
        AmenityDTO amenityDTO = amenityService.getAmenityById(id);
        return amenityDTO != null ? ResponseEntity.ok(amenityDTO) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public AmenityDTO createAmenity(@RequestBody AmenityDTO amenityDTO) {
        return amenityService.createAmenity(amenityDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AmenityDTO> updateAmenity(@PathVariable UUID id, @RequestBody AmenityDTO amenityDTO) {
        AmenityDTO updatedAmenity = amenityService.updateAmenity(id, amenityDTO);
        return updatedAmenity != null ? ResponseEntity.ok(updatedAmenity) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable UUID id) {
        amenityService.deleteAmenity(id);
        return ResponseEntity.noContent().build();
    }
}
