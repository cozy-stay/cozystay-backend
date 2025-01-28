package rw.cozy.cozybackend.dtos.request;

import lombok.Data;
import rw.cozy.cozybackend.enums.ERoomType;

import java.util.UUID;

@Data
public class RoomDTO {
    private UUID id;
    private String name;
    private ERoomType roomType;
}