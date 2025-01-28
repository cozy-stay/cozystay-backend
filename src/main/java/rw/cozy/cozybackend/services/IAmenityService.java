package rw.cozy.cozybackend.services;

import rw.cozy.cozybackend.dtos.request.AmenityDTO;

import java.util.List;
import java.util.UUID;

public interface IAmenityService {
    List<AmenityDTO> getAllAmenities();
    AmenityDTO getAmenityById(UUID id);
    AmenityDTO createAmenity(AmenityDTO amenityDTO);
    AmenityDTO updateAmenity(UUID id, AmenityDTO amenityDTO);
    void deleteAmenity(UUID id);
}
