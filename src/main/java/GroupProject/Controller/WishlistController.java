package GroupProject.Controller;

import GroupProject.Entity.Room;
import GroupProject.Entity.User;
import GroupProject.Service.RoomService;
import GroupProject.Service.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private RoomService roomService;

    // TOGGLE save/unsave a room
    @GetMapping("/toggle/{roomId}")
    public String toggleWishlist(@PathVariable Long roomId,
                                 HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Room room = roomService.getRoomById(roomId);
        wishlistService.toggleWishlist(user, room);

        return "redirect:/dashboard";
    }

    // VIEW wishlist page
    @GetMapping("")
    public String viewWishlist(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        List<Room> savedRooms = wishlistService.getWishlistRooms(user);
        model.addAttribute("rooms", savedRooms);
        model.addAttribute("loggedInUser", user);
        return "wishlist";
    }
}