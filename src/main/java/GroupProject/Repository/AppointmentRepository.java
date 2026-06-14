package GroupProject.Repository;

import GroupProject.Entity.Appointment;
import GroupProject.Entity.User;
import GroupProject.Entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // get all appointments booked by a tenant
    List<Appointment> findByTenant(User tenant);

    // get all appointments for a specific room
    List<Appointment> findByRoom(Room room);
}
