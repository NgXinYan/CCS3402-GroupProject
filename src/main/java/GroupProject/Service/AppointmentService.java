package GroupProject.Service;

import GroupProject.Entity.Appointment;
import GroupProject.Entity.Room;
import GroupProject.Entity.User;
import GroupProject.Repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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

    // CREATE & UPDATE - save appointment
    public void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    // DELETE - delete appointment by id
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}
