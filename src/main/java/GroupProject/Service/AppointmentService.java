package GroupProject.Service;

import GroupProject.Entity.Appointment;
import GroupProject.Entity.Room;
import GroupProject.Entity.User;
import GroupProject.Repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // READ - get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // READ - get appointments by tenant
    public List<Appointment> getAppointmentsByTenant(User tenant) {
        return appointmentRepository.findByTenant(tenant);
    }

    // READ - get appointments by room
    public List<Appointment> getAppointmentsByRoom(Room room) {
        return appointmentRepository.findByRoom(room);
    }

    // READ - get single appointment by id
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Appointment not found with id: " + id)
        );
    }

    // check if 30-min time slot is already taken
    public boolean isTimeSlotTaken(Room room, LocalDateTime dateTime) {
        // round down to nearest 30 min slot
        int minute = dateTime.getMinute() >= 30 ? 30 : 0;
        LocalDateTime slotStart = dateTime.withMinute(minute).withSecond(0).withNano(0);
        LocalDateTime slotEnd = slotStart.plusMinutes(30);
        return appointmentRepository.existsByRoomAndTimeSlot(room, slotStart, slotEnd);
    }

    // CREATE & UPDATE - save appointment
    public void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    // DELETE - delete appointment by id
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}
