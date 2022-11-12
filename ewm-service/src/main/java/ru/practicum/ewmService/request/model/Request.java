package ru.practicum.ewmService.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Builder.Default
    LocalDateTime created = LocalDateTime.now();
    @ManyToOne(cascade = CascadeType.ALL)
    Event event;
    @ManyToOne(cascade = CascadeType.ALL)
    User requester;
    String status;
}
