package com.feedsome.service;

import com.feedsome.model.Category;
import com.feedsome.service.exception.DuplicateServiceException;
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
     * Provides all the available categories, as they have been defined by the plugins
     * @param pageable the pagination criteria
     * @return a {@link Page<Category>} with the requested {@link Category} instances.
     */
    @NotNull
    Page<Category> getAll(@NotNull Pageable pageable);

    /**
     * Creates and persists a category to the system
     * @param category the {@link Category} instance to be created
     * @return an {@link Optional<Category>} that wraps an actual {@link Category} instance,
     *         or {@link Optional#empty()} if the creation process was not completed successfully.
     */
    @NotNull
    @UnwrapValidatedValue
    Optional<Category> create(@NotNull @Valid Category category) throws DuplicateServiceException;
}
