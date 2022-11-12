package ru.practicum.ewmService.event.compilation.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmService.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@Table(name = "compilations")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @ManyToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Event> events;
    Boolean pinned;
    String title;
}
