package com.feedsome.service;

import com.feedsome.model.Category;
import com.feedsome.repository.CategoryRepository;
import com.feedsome.service.exception.DuplicateServiceException;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Validated
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(@NotNull CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @NotNull
    public Page<Category> getAll(@NotNull final Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    @NotNull
    @UnwrapValidatedValue
    public Optional<Category> create(@NotNull @Valid final Category category) throws DuplicateServiceException {
        final Optional<Category> persistedCategory = categoryRepository.findByNameIgnoreCase(category.getName());
        if(persistedCategory.isPresent()) {
            throw new DuplicateServiceException();
        }

        return Optional.ofNullable(categoryRepository.save(category));
    }

}
