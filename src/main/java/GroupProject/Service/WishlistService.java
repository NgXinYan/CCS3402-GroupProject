package GroupProject.Service;

import GroupProject.Entity.Room;
import GroupProject.Entity.User;
import GroupProject.Entity.Wishlist;
import GroupProject.Repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    // get all rooms saved by user
    public List<Room> getWishlistRooms(User user) {
        return wishlistRepository.findByUser(user)
                .stream()
                .map(Wishlist::getRoom)
                .collect(Collectors.toList());
    }

    // check if room is saved
    public boolean isSaved(User user, Room room) {
        return wishlistRepository.existsByUserAndRoom(user, room);
    }

    // toggle save/unsave
    public boolean toggleWishlist(User user, Room room) {
        var existing = wishlistRepository.findByUserAndRoom(user, room);
        if (existing.isPresent()) {
            wishlistRepository.delete(existing.get());
            return false;        // now unsaved
        } else {
            Wishlist wishlist = new Wishlist();
            wishlist.setUser(user);
            wishlist.setRoom(room);
            wishlistRepository.save(wishlist);
            return true;         // now saved
        }
    }

    // delete all wishlist entries for a room (used before deleting a room)
    public void deleteByRoom(Room room) {
        List<Wishlist> entries = wishlistRepository.findByRoom(room);
        wishlistRepository.deleteAll(entries);
    }

    // delete all wishlist entries by a user
    public void deleteByUser(User user) {
        List<Wishlist> entries = wishlistRepository.findByUser(user);
        wishlistRepository.deleteAll(entries);
    }
}