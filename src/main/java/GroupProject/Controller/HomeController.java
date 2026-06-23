package GroupProject.Controller;

import GroupProject.Entity.Room;
import GroupProject.Entity.User;
import GroupProject.Service.AppointmentService;
import GroupProject.Service.RoomService;
import GroupProject.Service.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/")
    public String home(HttpSession session){
        User user = (User) session.getAttribute("loggedInUser");
        if(user == null){
            return "redirect:/login";
        }
        return "redirect/dashboard";
    }

    //DASHBOARD
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session,
                            Model model,
                            @RequestParam(required = false) String location,
                            @RequestParam(required = false) String type,
                            @RequestParam(required = false) String status,
                            @RequestParam(required = false) List<String> facilities,
                            @RequestParam(required = false) Integer carParkLots) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        List<Room> rooms = roomService.searchRooms(location, type, status, facilities, carParkLots);

        model.addAttribute("rooms", rooms);
        model.addAttribute("loggedInUser", user);
        model.addAttribute("totalRooms", roomService.getAllRooms().size());
        model.addAttribute("myRooms", roomService.getRoomsByOwner(user).size());
        model.addAttribute("myAppointments",
                appointmentService.getAppointmentsByTenant(user).size());
        model.addAttribute("location", location);
        model.addAttribute("type", type);
        model.addAttribute("status", status);
        model.addAttribute("facilities", facilities);
        model.addAttribute("carParkLots", carParkLots);

        // get wishlist room ids for heart icon state
        List<Long> wishlistRoomIds = wishlistService.getWishlistRooms(user)
                .stream().map(Room::getId).toList();
        model.addAttribute("wishlistRoomIds", wishlistRoomIds);

        return "dashboard";
    }
}
