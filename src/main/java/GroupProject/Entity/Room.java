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
    private String title;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // ── UNIT DETAILS (for Whole Unit) ──
    @Column(name = "bedrooms")
    private Integer bedrooms;               // number of bedrooms

    @Column(name = "bathrooms")
    private Integer bathrooms;              // number of bathrooms

    // ── FACILITIES ──
    @Column(name = "fully_furnished")
    private Boolean fullyFurnished;         // fully furnished

    @Column(name = "wifi_included")
    private Boolean wifiIncluded;           // wifi included

    @Column(name = "air_cond")
    private Boolean airCond;                // air conditioning

    @Column(name = "digital_door_lock")
    private Boolean digitalDoorLock;        // digital door lock

    @Column(name = "car_park")
    private Boolean carPark;                // car park available

    @Column(name = "car_park_lots")
    private Integer carParkLots;            // number of car park lots

    @Column(name = "private_bathroom")
    private Boolean privateBathroom;        // private bathroom

    @Column(name = "shared_bathroom")
    private Boolean sharedBathroom;         // shared bathroom

    @Column(name = "utilities_included")
    private Boolean utilitiesIncluded;      // utilities included in price

    @Column(name = "cooking_allowed")
    private Boolean cookingAllowed;         // cooking allowed

    @Column(name = "pets_allowed")
    private Boolean petsAllowed;            // pets allowed

    @Column(name = "no_owner_stays")
    private Boolean noOwnerStays;           // no owner stays in unit

    @Column(name = "service_residence")
    private Boolean serviceResidence;       // service residence

    @Column(name = "water_heater")
    private Boolean waterHeater;            // water heater

    @Column(name = "washing_machine")
    private Boolean washingMachine;         // washing machine

    @Column(name = "other_facilities", length = 500)
    private String otherFacilities;

    @ManyToOne
    @JoinColumn(name = "current_tenant_id")
    private User currentTenant;             // tenant who is renting this room (set when not available)// custom facilities by owner


}
