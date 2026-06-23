package GroupProject.Service;

import GroupProject.Entity.Room;
import GroupProject.Entity.User;
import GroupProject.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
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

    //SEARCH - search room by location, type, status, facilities, car park lots
    public List<Room> searchRooms(String location, String type, String status, List<String> facilities, Integer carParkLots) {
        Specification<Room> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (location != null && !location.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
            }
            if (type != null && !type.isEmpty()) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (carParkLots != null && carParkLots > 0) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("carParkLots"), carParkLots));
            }
            if (facilities != null && !facilities.isEmpty()) {
                for (String facility : facilities) {
                    predicates.add(cb.isTrue(root.get(facility)));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return roomRepository.findAll(spec);
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
