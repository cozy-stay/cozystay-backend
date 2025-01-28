package rw.cozy.cozybackend.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.cozy.cozybackend.dtos.request.AddressDTO;
import rw.cozy.cozybackend.services.IAddressService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/address")
@AllArgsConstructor
public class AddressController {

    private IAddressService addressService;

    @GetMapping
    public List<AddressDTO> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable UUID id) {
        AddressDTO addressDTO = addressService.getAddressById(id);
        return addressDTO != null ? ResponseEntity.ok(addressDTO) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public AddressDTO createAddress(@RequestBody AddressDTO addressDTO) {
        return addressService.createAddress(addressDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable UUID id, @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddress = addressService.updateAddress(id, addressDTO);
        return updatedAddress != null ? ResponseEntity.ok(updatedAddress) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}