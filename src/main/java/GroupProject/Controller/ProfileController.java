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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private WishlistService wishlistService;

    // SHOW profile page
    @GetMapping
    public String showProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        model.addAttribute("loggedInUser", user);
        return "profile";
    }

    // UPDATE profile info (name, phone, gender)
    @PostMapping("/update")
    public String updateProfile(@RequestParam String name,
                                @RequestParam String phone,
                                @RequestParam String gender,
                                HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        user.setName(name);
        user.setPhone(phone);
        user.setGender(gender);
        userService.updateProfile(user);

        session.setAttribute("loggedInUser", user);   // refresh session data
        return "redirect:/profile?updated=true";
    }

    // RESET password
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String currentPassword,
                                @RequestParam String newPassword,
                                HttpSession session,
                                Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        if (!user.getPassword().equals(currentPassword)) {
            model.addAttribute("loggedInUser", user);
            model.addAttribute("passwordError", "Current password is incorrect!");
            return "profile";
        }

        user.setPassword(newPassword);
        userService.updateProfile(user);
        session.setAttribute("loggedInUser", user);

        return "redirect:/profile?passwordChanged=true";
    }

    // DELETE account
    @GetMapping("/delete")
    public String deleteAccount(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        // 1. delete all appointments where user is tenant
        List<Appointment> myBookings = appointmentService.getAppointmentsByTenant(user);
        for (Appointment apt : myBookings) {
            appointmentService.deleteAppointment(apt.getId());
        }

        // 2. delete all wishlist entries by this user
        wishlistService.deleteByUser(user);

        // 3. for rooms owned by this user — delete their appointments, wishlists, then rooms
        List<Room> myRooms = roomService.getRoomsByOwner(user);
        for (Room room : myRooms) {
            List<Appointment> roomAppointments = appointmentService.getAppointmentsByRoom(room);
            for (Appointment apt : roomAppointments) {
                appointmentService.deleteAppointment(apt.getId());
            }
            wishlistService.deleteByRoom(room);
            roomService.deleteRoomById(room.getId());
        }

        // 4. finally delete the user
        userService.deleteAccount(user.getId());

        session.invalidate();
        return "redirect:/login?accountDeleted=true";
    }
}
