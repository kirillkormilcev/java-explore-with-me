package ru.practicum.ewmService.place;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmService.place.model.Place;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("select p from Place p " +
            "where upper(p.name) like upper(concat('%', ?1, '%')) " +
            "and p.available is true")
    List<Place> findAllByName(String name);

    @Query("select p from Place p " +
            "where upper(p.placeType.name) like upper(concat('%', ?1, '%')) " +
            "and p.available is true")
    List<Place> findAllByPlaceTypes(String placeTypes);

    @Query("select p from Place p " +
            "where upper(p.name) like upper(concat('%', ?1, '%')) " +
            "and upper(p.placeType.name) like upper(concat('%', ?2, '%')) " +
            "and p.available is true")
    List<Place> findAllByNameAndPlaceType(String name, String placeTypes);
}
