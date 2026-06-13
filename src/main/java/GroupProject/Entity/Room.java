package GroupProject.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data //lombok auto generate getter setter and toString
@Entity
@Table(name = "room") //table name
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_seq")
    @SequenceGenerator(name = "room_seq", sequenceName = "room_sequence", allocationSize = 1)
    private Long id;

    //room title
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "type", nullable = false)
    private String type; //eg. single, master

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "status", nullable = false)
    private String status; //available or occupied

    @Column(name = "description")
    private String description; //optional details
}
