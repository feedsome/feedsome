package com.feedsome.service;

import com.feedsome.model.Category;
import com.feedsome.model.Feed;
import com.feedsome.model.Plugin;
import com.feedsome.repository.CategoryRepository;
import com.feedsome.repository.FeedRepository;
import com.feedsome.repository.PluginRepository;
import com.feedsome.service.exception.NotFoundServiceException;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Validated
public class FeedServiceImpl implements FeedService {

    private final PluginRepository pluginRepository;
    private final CategoryRepository categoryRepository;

    private final FeedRepository feedRepository;


    public FeedServiceImpl(@NotNull final PluginRepository pluginRepository,
                           @NotNull final CategoryRepository categoryRepository,
                           @NotNull final FeedRepository feedRepository) {
        this.pluginRepository = pluginRepository;
        this.categoryRepository = categoryRepository;
        this.feedRepository = feedRepository;
    }

    @Override
    @NotNull
    @UnwrapValidatedValue
    public Feed persist(@NotNull final Feed feedNotification) throws NotFoundServiceException {
        final Optional<Plugin> plugin = pluginRepository.findByName(feedNotification.getPlugin().getName());
        if(!plugin.isPresent()) {
            throw new NotFoundServiceException();
        }

        final Set<String> categoryNames = feedNotification.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toSet());

        final Collection<Category> categories = categoryRepository.findByNameIn(categoryNames);
        if(categories.isEmpty()) {
            throw new NotFoundServiceException();
        }

        final Feed feed = new Feed();
        feed.setPlugin(plugin.get());
        feed.setTitle(feedNotification.getTitle());
        feed.setBody(feedNotification.getBody());

        feed.getCategories().addAll(categories);

        return feedRepository.save(feed);
    }
}
