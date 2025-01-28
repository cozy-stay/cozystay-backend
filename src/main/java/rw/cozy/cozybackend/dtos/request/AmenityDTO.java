package rw.cozy.cozybackend.dtos.request;

import lombok.Data;

import java.util.UUID;

@Data
public class AmenityDTO {
    private UUID id;
    private String name;
}