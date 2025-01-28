package rw.cozy.cozybackend.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rw.cozy.cozybackend.dtos.request.RoomDTO;
import rw.cozy.cozybackend.model.Room;
import rw.cozy.cozybackend.repository.IRoomRepository;
import rw.cozy.cozybackend.services.IRoomService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements IRoomService {

    private IRoomRepository roomRepository;

    @Override
    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public RoomDTO getRoomById(UUID id) {
        return roomRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Override
    public RoomDTO createRoom(RoomDTO roomDTO) {
        Room room = convertToEntity(roomDTO);
        room = roomRepository.save(room);
        return convertToDTO(room);
    }

    @Override
    public RoomDTO updateRoom(UUID id, RoomDTO roomDTO) {
        Room room = convertToEntity(roomDTO);
        room.setId(id);
        room = roomRepository.save(room);
        return convertToDTO(room);
    }

    @Override
    public void deleteRoom(UUID id) {
        roomRepository.deleteById(id);
    }

    private RoomDTO convertToDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setName(room.getName());
        roomDTO.setRoomType(room.getRoomType());
        return roomDTO;
    }

    private Room convertToEntity(RoomDTO roomDTO) {
        Room room = new Room();
        room.setName(roomDTO.getName());
        room.setRoomType(roomDTO.getRoomType());
        return room;
    }
}
