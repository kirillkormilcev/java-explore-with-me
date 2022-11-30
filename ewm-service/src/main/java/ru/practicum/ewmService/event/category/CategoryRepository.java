package ru.practicum.ewmService.event.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmService.event.category.model.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByIdIn(List<Long> categories);
}
