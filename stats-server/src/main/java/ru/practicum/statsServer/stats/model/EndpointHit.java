package ru.practicum.statsServer.stats.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@Table(name = "hits")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @NotNull
    @Column(length = 255)
    String app;
    @NotNull
    @Column(length = 255)
    String uri;
    @NotNull
    @Column(length = 255)
    String ip;
    @NotNull
    LocalDateTime timestamp;
}
