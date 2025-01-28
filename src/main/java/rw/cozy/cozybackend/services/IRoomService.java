package rw.cozy.cozybackend.services;

import rw.cozy.cozybackend.dtos.request.RoomDTO;

import java.util.List;
import java.util.UUID;

public interface IRoomService {
    List<RoomDTO> getAllRooms();
    RoomDTO getRoomById(UUID id);
    RoomDTO createRoom(RoomDTO roomDTO);
    RoomDTO updateRoom(UUID id, RoomDTO roomDTO);
    void deleteRoom(UUID id);
}
