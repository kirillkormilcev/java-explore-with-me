package ru.practicum.ewmService.event.category.access.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.event.category.dto.CategoryDto;
import ru.practicum.ewmService.event.category.dto.CategoryNewDto;

@Slf4j
@RestController("AdminCategoryController")
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class CategoryController {
    final CategoryService categoryService;

    @PatchMapping
    public ResponseEntity<CategoryDto> updateCategoryByAdmin(
            @Validated @RequestBody CategoryDto categoryDto) {
        log.info("Обработка эндпойнта PATCH/admin/categories.(body: categoryDto)");
        return new ResponseEntity<>(categoryService.updateCategoryByAdmin(categoryDto), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategoryByAdmin(
            @Validated @RequestBody CategoryNewDto categoryNewDto) {
        log.info("Обработка эндпойнта POST/admin/categories.(body: categoryNewDto)");
        return new ResponseEntity<>(categoryService.createCategoryByAdmin(categoryNewDto), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public HttpStatus deleteCategory(
            @PathVariable long categoryId) {
        log.info("Обработка эндпойнта DELETE/admin/categories/" + categoryId + ".");
        categoryService.deleteCategory(categoryId);
        return HttpStatus.OK;
    }
}
