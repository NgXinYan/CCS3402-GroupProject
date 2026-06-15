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

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private User tenant;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "status", nullable = false)
    private String status;              // Pending, Confirmed, Rejected, Cancelled

    @Column(name = "notes")
    private String notes;               // tenant notes

    @Column(name = "reject_reason")
    private String rejectReason;        // owner rejection reason

    @Column(name = "suggested_time_from")
    private LocalDateTime suggestedTimeFrom;    // owner suggested time start

    @Column(name = "suggested_time_to")
    private LocalDateTime suggestedTimeTo;      // owner suggested time end
}