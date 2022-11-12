package ru.practicum.ewmService.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmService.event.category.model.Category;
import ru.practicum.ewmService.event.location.model.Location;
import ru.practicum.ewmService.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(length = 1024)
    @NotNull
    String annotation;
    @ManyToOne(cascade = CascadeType.ALL)
    Category category;
    int confirmedRequests;
    @Builder.Default
    LocalDateTime createdOn = LocalDateTime.now();
    @Column(length = 2056)
    String description;
    @NotNull
    LocalDateTime eventDate;
    @ManyToOne(cascade = CascadeType.ALL)
    User initiator;
    @OneToOne(cascade = CascadeType.ALL)
    Location location;
    @NotNull
    Boolean paid;
    Integer participantLimit;
    LocalDateTime publishedOn;
    Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    State state = State.PENDING;
    @Column(length = 1024)
    @NotNull
    String title;
    long views;
    @Builder.Default
    Boolean available = true;
}
