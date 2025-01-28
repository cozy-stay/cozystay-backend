package rw.cozy.cozybackend.dtos.request;

import lombok.Data;

import java.util.UUID;

@Data
public class AddressDTO {
    private String street;
    private String city;
    private String country;
    private String zipCode;
    private double latitude;
    private double longitude;
}