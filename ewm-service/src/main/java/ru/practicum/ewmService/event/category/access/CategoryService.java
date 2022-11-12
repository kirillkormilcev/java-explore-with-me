package ru.practicum.ewmService.event.category.access;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.common.PageRequestModified;
import ru.practicum.ewmService.error.exception.NotFoundException;
import ru.practicum.ewmService.event.category.CategoryMapper;
import ru.practicum.ewmService.event.category.CategoryRepository;
import ru.practicum.ewmService.event.category.dto.CategoryDto;
import ru.practicum.ewmService.event.category.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@Service("PublicCategoryService")
@Transactional(readOnly = true)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryService {
    final CategoryRepository categoryRepository;

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        PageRequest pageRequest = new PageRequestModified(from, size, Sort.by("name"));
        return categoryRepository.findAll(pageRequest)
                .stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    public CategoryDto getCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Категория с индексом " + categoryId + " не найдена в базе."));
        return CategoryMapper.toCategoryDto(category);
    }
}
