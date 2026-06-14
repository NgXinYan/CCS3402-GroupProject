package GroupProject.Repository;

import GroupProject.Entity.Room;
import GroupProject.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // get all rooms owned by a specific user
    List<Room> findByOwner(User owner);

    // search rooms by location
    List<Room> findByLocationContainingIgnoreCase(String location);

    // filter rooms by type
    List<Room> findByType(String type);

    // filter rooms by status
    List<Room> findByStatus(String status);

    // search by location and type
    List<Room> findByLocationContainingIgnoreCaseAndType(String location, String type);
}
