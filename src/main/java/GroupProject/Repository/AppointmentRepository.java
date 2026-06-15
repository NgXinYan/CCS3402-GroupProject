package GroupProject.Repository;

import GroupProject.Entity.Appointment;
import GroupProject.Entity.User;
import GroupProject.Entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // get all appointments booked by a tenant
    List<Appointment> findByTenant(User tenant);

    // get all appointments for a specific room
    List<Appointment> findByRoom(Room room);

    // check if any active appointment exists within 30 min of requested time
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.room = :room " +
            "AND a.status != 'Cancelled' " +
            "AND a.status != 'Rejected' " +
            "AND a.dateTime >= :slotStart " +
            "AND a.dateTime < :slotEnd")
    boolean existsByRoomAndTimeSlot(@Param("room") Room room,
                                    @Param("slotStart") LocalDateTime slotStart,
                                    @Param("slotEnd") LocalDateTime slotEnd);
}
