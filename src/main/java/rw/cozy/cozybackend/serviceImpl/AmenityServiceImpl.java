package rw.cozy.cozybackend.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rw.cozy.cozybackend.dtos.request.AmenityDTO;
import rw.cozy.cozybackend.model.Amenity;
import rw.cozy.cozybackend.repository.IAmenityRepository;
import rw.cozy.cozybackend.services.IAmenityService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AmenityServiceImpl implements IAmenityService {

    private IAmenityRepository amenityRepository;

    @Override
    public List<AmenityDTO> getAllAmenities() {
        return amenityRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public AmenityDTO getAmenityById(UUID id) {
        return amenityRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Override
    public AmenityDTO createAmenity(AmenityDTO amenityDTO) {
        Amenity amenity = convertToEntity(amenityDTO);
        amenity = amenityRepository.save(amenity);
        return convertToDTO(amenity);
    }

    @Override
    public AmenityDTO updateAmenity(UUID id, AmenityDTO amenityDTO) {
        Amenity amenity = convertToEntity(amenityDTO);
        amenity.setId(id);
        amenity = amenityRepository.save(amenity);
        return convertToDTO(amenity);
    }

    @Override
    public void deleteAmenity(UUID id) {
        amenityRepository.deleteById(id);
    }

    private AmenityDTO convertToDTO(Amenity amenity) {
        AmenityDTO amenityDTO = new AmenityDTO();
        amenityDTO.setId(amenity.getId());
        amenityDTO.setName(amenity.getName());
        return amenityDTO;
    }

    private Amenity convertToEntity(AmenityDTO amenityDTO) {
        Amenity amenity = new Amenity();
        amenity.setName(amenityDTO.getName());
        return amenity;
    }
}