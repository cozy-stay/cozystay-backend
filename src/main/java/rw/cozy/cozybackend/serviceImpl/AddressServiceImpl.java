package rw.cozy.cozybackend.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rw.cozy.cozybackend.dtos.request.AddressDTO;
import rw.cozy.cozybackend.model.Address;
import rw.cozy.cozybackend.repository.IAddressRepository;
import rw.cozy.cozybackend.services.IAddressService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements IAddressService {

    private IAddressRepository addressRepository;

    @Override
    public List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public AddressDTO getAddressById(UUID id) {
        return addressRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Address address = convertToEntity(addressDTO);
        address = addressRepository.save(address);
        return convertToDTO(address);
    }

    @Override
    public AddressDTO updateAddress(UUID id, AddressDTO addressDTO) {
        Address address = convertToEntity(addressDTO);
        address.setId(id);
        address = addressRepository.save(address);
        return convertToDTO(address);
    }

    @Override
    public void deleteAddress(UUID id) {
        addressRepository.deleteById(id);
    }

    private AddressDTO convertToDTO(Address address) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setCountry(address.getCountry());
        addressDTO.setZipCode(address.getZipCode());
        return addressDTO;
    }

    private Address convertToEntity(AddressDTO addressDTO) {
        Address address = new Address();
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setCountry(addressDTO.getCountry());
        address.setZipCode(addressDTO.getZipCode());
        return address;
    }
}