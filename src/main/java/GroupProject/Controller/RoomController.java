package GroupProject.Controller;

import GroupProject.Entity.Appointment;
import GroupProject.Entity.Room;
import GroupProject.Entity.User;
import GroupProject.Service.AppointmentService;
import GroupProject.Service.RoomService;
import GroupProject.Service.UserService;
import GroupProject.Service.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private UserService userService;

    //ROOM DETAIL PAGE
    @GetMapping("/{id}")
    public String roomDetail(@PathVariable Long id, HttpSession session, Model model){
        User user = (User) session.getAttribute("loggedInUser");
        if(user == null){
            return "redirect:/login";
        }

        Room room = roomService.getRoomById(id);
        List<Appointment> appointments = appointmentService.getAppointmentsByRoom(room);

        model.addAttribute("room", room);
        model.addAttribute("appointments", appointments);
        model.addAttribute("loggedInUser", user);
        model.addAttribute("newAppointment", new Appointment());
        model.addAttribute("isInWishlist", wishlistService.isSaved(user, room));
        return "room-detail";
    }

    //SHOW ADD ROOM FORM
    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model){
        User user = (User) session.getAttribute("loggedInUser");
        if(user == null) return "redirect:/login";

        model.addAttribute("room", new Room());
        model.addAttribute("loggedInUser", user);
        return "add-room";
    }

    //SAVE NEW ROOM
    @PostMapping("/save")
    public String saveRoom(@ModelAttribute Room room, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        room.setOwner(user);
        room.setStatus("Available");
        roomService.saveRoom(room);
        return "redirect:/rooms/my-rooms"; //change here
    }

    //EDIT ROOM FORM
    @GetMapping("/edit/{id}")
    public String showEdirForm(@PathVariable Long id, HttpSession session, Model model){
        User user = (User) session.getAttribute("loggedInUser");
        if(user == null) return "redirect:/login";

        Room room = roomService.getRoomById(id);
        model.addAttribute("room", room);
        model.addAttribute("loggedInUser", user);
        return "add-room";
    }

    // UPDATE ROOM
    @PostMapping("/update/{id}")
    public String updateRoom(@PathVariable Long id,
                             @ModelAttribute Room room,
                             HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        room.setId(id);
        room.setOwner(user);
        roomService.saveRoom(room);
        return "redirect:/rooms/my-rooms"; //here
    }

    // DELETE ROOM
    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Room room = roomService.getRoomById(id);

        // delete all appointments for this room first
        List<Appointment> appointments = appointmentService.getAppointmentsByRoom(room);
        for (Appointment apt : appointments) {
            appointmentService.deleteAppointment(apt.getId());
        }

        // delete all wishlist entries for this room
        wishlistService.deleteByRoom(room);

        // then delete the room
        roomService.deleteRoomById(id);
        return "redirect:/rooms/my-rooms";
    }

    // MY ROOMS PAGE
    @GetMapping("/my-rooms")
    public String myRooms(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        List<Room> myRooms = roomService.getRoomsByOwner(user);
        model.addAttribute("rooms", myRooms);
        model.addAttribute("loggedInUser", user);
        return "my-rooms";
    }

    // BOOK APPOINTMENT
    @PostMapping("/{id}/book")
    public String bookAppointment(@PathVariable Long id,
                                  @RequestParam String dateTime,
                                  @RequestParam(required = false) String notes,
                                  HttpSession session,
                                  Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Room room = roomService.getRoomById(id);
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime);

        // check if time slot already taken
        if (appointmentService.isTimeSlotTaken(room, parsedDateTime)) {
            model.addAttribute("room", room);
            model.addAttribute("appointments",
                    appointmentService.getAppointmentsByRoom(room));
            model.addAttribute("loggedInUser", user);
            model.addAttribute("newAppointment", new Appointment());
            model.addAttribute("timeError",
                    "This time slot is already booked! Please choose another time.");
            return "room-detail";
        }

        Appointment appointment = new Appointment();
        appointment.setRoom(room);
        appointment.setTenant(user);
        appointment.setDateTime(parsedDateTime);
        appointment.setStatus("Pending");
        appointment.setNotes(notes);
        appointmentService.saveAppointment(appointment);

        return "redirect:/rooms/" + id + "?success=true";
    }

    // CHANGE ROOM STATUS (owner only)
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               @RequestParam(required = false) String tenantEmail,
                               HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Room room = roomService.getRoomById(id);

        if (!room.getOwner().getId().equals(user.getId())) {
            return "redirect:/rooms/" + id + "?error=notowner";
        }

        room.setStatus(status);

        // if marking as Occupied/Not Available, try to link tenant
        if (status.equals("Occupied") && tenantEmail != null && !tenantEmail.isEmpty()) {
            User tenant = userService.findByEmail(tenantEmail);
            if (tenant != null) {
                room.setCurrentTenant(tenant);
            }
        }

        // if marking back as Available, clear tenant
        if (status.equals("Available")) {
            room.setCurrentTenant(null);
        }

        roomService.saveRoom(room);
        return "redirect:/rooms/my-rooms";
    }
}
