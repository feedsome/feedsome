package com.feedsome.service;

import com.feedsome.model.Feed;
import com.feedsome.model.FeedNotification;
import com.feedsome.model.Plugin;
import com.feedsome.repository.FeedRepository;
import com.feedsome.repository.PluginRepository;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Validated
public class FeedServiceImpl implements FeedService {

    private final PluginRepository pluginRepository;
    private final FeedRepository feedRepository;

    public FeedServiceImpl(@NotNull final PluginRepository pluginRepository,
                           @NotNull final FeedRepository feedRepository) {
        this.pluginRepository = pluginRepository;
        this.feedRepository = feedRepository;
    }

    @Override
    @NotNull
    @UnwrapValidatedValue
    public Feed persistNotification(@NotNull final FeedNotification feedNotification) {
        Optional<Plugin> plugin = pluginRepository.findByName(feedNotification.getPluginRef());
        if(!plugin.isPresent()) {
            //TODO: handle (exception to send to dead channel)
            throw new RuntimeException();
        }

        final Feed feed = Feed.builder()
                .pluginReference(plugin.get())
                .title(feedNotification.getTitle())
                .description(feedNotification.getDescription())
                .body(feedNotification.getBody())
                .build();

        return feedRepository.save(feed);
    }
}
