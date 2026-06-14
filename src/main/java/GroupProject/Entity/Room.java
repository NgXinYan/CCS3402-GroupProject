package GroupProject.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_seq")
    @SequenceGenerator(name = "room_seq", sequenceName = "room_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;                   // room title

    @Column(name = "type", nullable = false)
    private String type;                    // Single, Master, Whole Unit

    @Column(name = "price", nullable = false)
    private Double price;                   // monthly price

    @Column(name = "location", nullable = false)
    private String location;                // address or area

    @Column(name = "status", nullable = false)
    private String status;                  // Available, Occupied, Pending

    @Column(name = "description")
    private String description;             // room details

    @ManyToOne                              // many rooms can belong to one owner
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;                     // the owner of this room
}
