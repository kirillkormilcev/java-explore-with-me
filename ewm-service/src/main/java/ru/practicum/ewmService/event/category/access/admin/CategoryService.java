package ru.practicum.ewmService.event.category.access.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.error.exception.CategoryValidationException;
import ru.practicum.ewmService.error.exception.NotFoundException;
import ru.practicum.ewmService.event.EventRepository;
import ru.practicum.ewmService.event.category.CategoryMapper;
import ru.practicum.ewmService.event.category.CategoryRepository;
import ru.practicum.ewmService.event.category.dto.CategoryDto;
import ru.practicum.ewmService.event.category.dto.CategoryNewDto;
import ru.practicum.ewmService.event.model.Event;

import java.util.List;

@Service("AdminCategoryService")
@Transactional(readOnly = true)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryService {
    final CategoryRepository categoryRepository;
    final EventRepository eventRepository;

    @Transactional
    public CategoryDto updateCategoryByAdmin(CategoryDto categoryDto) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategoryFromCategoryDto(categoryDto)));
    }

    @Transactional
    public CategoryDto createCategoryByAdmin(CategoryNewDto categoryNewDto) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategoryFromCategoryNewDto(categoryNewDto)));
    }

    @Transactional
    public void deleteCategory(long categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Категория с индексом " + categoryId + " не найдена в базе."));
        List<Event> events = eventRepository.findAllByCategoryId(categoryId);
        if (!eventRepository.findAllByCategoryId(categoryId).isEmpty()) {
            throw new CategoryValidationException("Категория с индексом " + categoryId + "не удалена," +
                    "т.к. она используется в событиях: " + events);
        }
        categoryRepository.deleteById(categoryId);
    }
}
