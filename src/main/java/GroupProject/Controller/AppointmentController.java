package GroupProject.Controller;

import GroupProject.Entity.Appointment;
import GroupProject.Entity.Room;
import GroupProject.Entity.User;
import GroupProject.Service.AppointmentService;
import GroupProject.Service.RoomService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private RoomService roomService;

    // MY APPOINTMENTS PAGE — shows both booked and incoming
    @GetMapping("/my")
    public String myAppointments(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        // appointments I booked as tenant
        List<Appointment> myBookings =
                appointmentService.getAppointmentsByTenant(user);

        // appointments on my rooms as owner
        List<Room> myRooms = roomService.getRoomsByOwner(user);
        List<Appointment> incomingRequests = new java.util.ArrayList<>();
        for (Room room : myRooms) {
            incomingRequests.addAll(appointmentService.getAppointmentsByRoom(room));
        }

        model.addAttribute("myBookings", myBookings);
        model.addAttribute("incomingRequests", incomingRequests);
        model.addAttribute("loggedInUser", user);
        return "my-appointments";
    }

    // ROOM APPOINTMENTS PAGE (as owner)
    @GetMapping("/room/{roomId}")
    public String roomAppointments(@PathVariable Long roomId,
                                   HttpSession session,
                                   Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Room room = roomService.getRoomById(roomId);
        List<Appointment> appointments =
                appointmentService.getAppointmentsByRoom(room);
        model.addAttribute("appointments", appointments);
        model.addAttribute("room", room);
        model.addAttribute("loggedInUser", user);
        return "room-appointments";
    }

    // CONFIRM appointment (owner action)
    @GetMapping("/confirm/{id}")
    public String confirmAppointment(@PathVariable Long id,
                                     HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Appointment apt = appointmentService.getAppointmentById(id);
        apt.setStatus("Confirmed");
        appointmentService.saveAppointment(apt);

        // do NOT change room status to Occupied
        // room stays available for other appointments

        return "redirect:/appointments/my?confirmed=true";
    }

    // SHOW REJECT FORM (owner action)
    @GetMapping("/reject/{id}")
    public String showRejectForm(@PathVariable Long id,
                                 HttpSession session,
                                 Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        model.addAttribute("appointment",
                appointmentService.getAppointmentById(id));
        model.addAttribute("loggedInUser", user);
        return "reject-appointment";
    }

    // PROCESS REJECTION (owner action)
    @PostMapping("/reject/{id}")
    public String processReject(@PathVariable Long id,
                                @RequestParam String reason,
                                @RequestParam(required = false) String suggestedTimeFrom,
                                @RequestParam(required = false) String suggestedTimeTo,
                                HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Appointment apt = appointmentService.getAppointmentById(id);
        apt.setStatus("Rejected");
        apt.setRejectReason(reason);

        if (suggestedTimeFrom != null && !suggestedTimeFrom.isEmpty()) {
            apt.setSuggestedTimeFrom(LocalDateTime.parse(suggestedTimeFrom));
        }
        if (suggestedTimeTo != null && !suggestedTimeTo.isEmpty()) {
            apt.setSuggestedTimeTo(LocalDateTime.parse(suggestedTimeTo));
        }

        appointmentService.saveAppointment(apt);

        return "redirect:/appointments/my?rejected=true";
    }

    // CANCEL appointment (tenant action)
    @GetMapping("/cancel/{id}")
    public String cancelAppointment(@PathVariable Long id,
                                    HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Appointment apt = appointmentService.getAppointmentById(id);
        apt.setStatus("Cancelled");
        appointmentService.saveAppointment(apt);

        // revert room to Available if was Occupied or Pending
        Room room = apt.getRoom();
        if (!room.getStatus().equals("Available")) {
            room.setStatus("Available");
            roomService.saveRoom(room);
        }

        return "redirect:/appointments/my";
    }
}