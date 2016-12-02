package com.feedsome.service;

import com.feedsome.model.Category;
import com.feedsome.model.Feed;
import com.feedsome.model.FeedNotification;
import com.feedsome.model.Plugin;
import com.feedsome.repository.CategoryRepository;
import com.feedsome.repository.FeedRepository;
import com.feedsome.repository.PluginRepository;
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
    public Feed persistNotification(@NotNull final FeedNotification feedNotification) {
        final Optional<Plugin> plugin = pluginRepository.findByName(feedNotification.getPluginRef());
        if(!plugin.isPresent()) {
            //TODO: handle (exception to send to dead channel)
            throw new RuntimeException();
        }

        final Feed feed = Feed.builder()
                .plugin(plugin.get())
                .title(feedNotification.getTitle())
                .body(feedNotification.getBody())
                .build();

        final Collection<Category> categories = categoryRepository.findByNameIn(feedNotification.getCategories());

        feed.getCategories().addAll(categories);

        return feedRepository.save(feed);
    }
}
