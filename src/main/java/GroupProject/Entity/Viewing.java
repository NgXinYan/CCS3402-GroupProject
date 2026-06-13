package GroupProject.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "viewing")
public class Viewing {

    @Id@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "viewing_seq")
    @SequenceGenerator(name = "viewing_seq", sequenceName = "viewing_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "viewing_date", nullable = false)
    private LocalDateTime viewingDate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "notes")
    private String notes;
}
