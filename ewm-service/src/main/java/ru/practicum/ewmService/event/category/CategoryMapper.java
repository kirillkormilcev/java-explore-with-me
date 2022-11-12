package ru.practicum.ewmService.event.category;

import ru.practicum.ewmService.event.category.dto.CategoryDto;
import ru.practicum.ewmService.event.category.dto.CategoryNewDto;
import ru.practicum.ewmService.event.category.model.Category;

public class CategoryMapper {

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toCategoryFromCategoryDto(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    public static Category toCategoryFromCategoryNewDto(CategoryNewDto categoryNewDto) {
        return Category.builder()
                .name(categoryNewDto.getName())
                .build();
    }
}
