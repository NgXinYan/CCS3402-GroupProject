package GroupProject.Controller;

import GroupProject.Entity.User;
import GroupProject.Service.AppointmentService;
import GroupProject.Service.RoomService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private AppointmentService appointmentService;

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
    public String dashboard(HttpSession session, Model model,
                            @RequestParam(required = false) String location,
                            @RequestParam(required = false) String type){
        //check if logged in
        User user = (User) session.getAttribute("loggedInUser");
        if(user == null){
            return "redirect:/login";
        }

        //search or show all rooms
        model.addAttribute("rooms", roomService.searchRooms(location, type));
        model.addAttribute("loggedInUser", user);
        model.addAttribute("totalRooms", roomService.getAllRooms().size());
        model.addAttribute("myRooms", roomService.getRoomsByOwner(user).size());
        model.addAttribute("myAppointments", appointmentService.getAppointmentsByTenant(user).size());
        model.addAttribute("location", location);
        model.addAttribute("type", type);
        return "dashboard";
    }


}
