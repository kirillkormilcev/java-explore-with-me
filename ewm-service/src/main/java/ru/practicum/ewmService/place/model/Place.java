package ru.practicum.ewmService.place.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmService.event.location.model.Location;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@Table(name = "places")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @NotNull
    @Column(length = 256, unique = true)
    String name;
    @Column(length = 2056)
    String description;
    @ManyToOne(cascade = CascadeType.ALL)
    PlaceType placeType;
    @OneToOne(cascade = CascadeType.ALL)
    Location location;
    @NotNull
    Float radius;
    @Builder.Default
    Boolean available = true;

}
