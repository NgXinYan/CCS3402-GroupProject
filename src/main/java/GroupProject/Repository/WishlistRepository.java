package GroupProject.Repository;

import GroupProject.Entity.Room;
import GroupProject.Entity.User;
import GroupProject.Entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    // get all wishlist items for a user
    List<Wishlist> findByUser(User user);

    // check if room already saved by user
    Optional<Wishlist> findByUserAndRoom(User user, Room room);

    // check existence
    boolean existsByUserAndRoom(User user, Room room);

    // get all wishlist entries for a room
    List<Wishlist> findByRoom(Room room);
}