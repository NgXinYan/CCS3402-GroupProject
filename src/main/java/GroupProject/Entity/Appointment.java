package GroupProject.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_seq")
    @SequenceGenerator(name = "appointment_seq", sequenceName = "appointment_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne                                          // many appointments for one room
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne                                          // many appointments for one tenant
    @JoinColumn(name = "tenant_id", nullable = false)
    private User tenant;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;                     // appointment date and time

    @Column(name = "status", nullable = false)
    private String status;                              // Pending, Confirmed, Cancelled

    @Column(name = "notes")
    private String notes;                               // optional notes
}
