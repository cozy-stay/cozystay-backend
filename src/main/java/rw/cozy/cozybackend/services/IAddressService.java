package rw.cozy.cozybackend.services;

import rw.cozy.cozybackend.dtos.request.AddressDTO;

import java.util.List;
import java.util.UUID;

public interface IAddressService {
    List<AddressDTO> getAllAddresses();
    AddressDTO getAddressById(UUID id);
    AddressDTO createAddress(AddressDTO addressDTO);
    AddressDTO updateAddress(UUID id, AddressDTO addressDTO);
    void deleteAddress(UUID id);
}
