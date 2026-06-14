package GroupProject.Service;

import GroupProject.Entity.Room;
import GroupProject.Entity.User;
import GroupProject.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    //READ - get all rooms
    public List<Room> getAllRooms(){
        return roomRepository.findAll();
    }

    //READ - get single room by id
    public Room getRoomById(Long id){
        return roomRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Room not found with id: " + id)
        );
    }

    //READ - get rooms by owner
    public List<Room> getRoomsByOwner(User owner){
        return roomRepository.findByOwner(owner);
    }

    //SEARCH - search room by locations and type
    public List<Room> searchRooms(String location, String type) {
        if (location != null && !location.isEmpty() && type != null && !type.isEmpty()) {
            return roomRepository.findByLocationContainingIgnoreCaseAndType(location, type);
        } else if (location != null && !location.isEmpty()) {
            return roomRepository.findByLocationContainingIgnoreCase(location);
        } else if (type != null && !type.isEmpty()) {
            return roomRepository.findByType(type);
        }
        return roomRepository.findAll();    // return all if no filter
    }

    //CREATE & UPDATE - save room
    public void saveRoom(Room room){
        roomRepository.save(room);
    }

    //DELETE - delete room by id
    public void deleteRoomById(Long id){
        roomRepository.deleteById(id);
    }
}
