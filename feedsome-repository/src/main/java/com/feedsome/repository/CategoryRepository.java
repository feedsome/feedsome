package com.feedsome.repository;

import com.feedsome.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    /**
     * Tries to find an already persisted {@link Category} instance,
     * by the provided category name.
     * @param name the provided {@link String} representation of the {@link Category} name.
     * @return an {@link Optional<Category>} that wraps the actual instance,
     *         or {@link Optional#empty()}, if no category exists.
     */
    Optional<Category> findByName(String name);

    /**
     * Tries to find an already persisted {@link Category} instance,
     * by the provided category name, ignoring case.
     * @param name the provided {@link String} representation of the {@link Category} name.
     * @return an {@link Optional<Category>} that wraps the actual instance,
     *         or {@link Optional#empty()}, if no category exists.
     */
    Optional<Category> findByNameIgnoreCase(String name);

    /**
     * Returns all the categories that exist, identified by the given collection of names
     * @param names a {@link Collection<String>} of category names
     * @return a {@link Collection<Category>} with all found instances
     */
    Collection<Category> findByNameIn(Collection<String> names);

    /**
     * Returns all the categories that exist, identified by the given collection of names
     * @param names a {@link Collection<String>} of category names
     * @return a {@link Collection<Category>} with all found instances
     */
    Collection<Category> findByNameInIgnoreCase(Collection<String> names);


}
