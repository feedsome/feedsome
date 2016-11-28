package com.feedsome.service;

import com.feedsome.model.Category;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Defines behaviour for services that can apply action on {@link Category} instances.
 */
public interface CategoryService {

    /**
     *
     * @param pageable
     * @return
     */
    @NotNull
    Page<Category> getAll(@NotNull Pageable pageable);

    /**
     *
     * @param category
     * @return
     */
    @NotNull
    @UnwrapValidatedValue
    Optional<Category> create(@NotNull @Valid Category category);
}
