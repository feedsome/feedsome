package com.feedsome.service;

import com.feedsome.model.Feed;
import com.feedsome.model.FeedNotification;

import javax.validation.constraints.NotNull;

/**
 * Defines behaviour for services that handle actions for acquired data {@link Feed} instances
 */
public interface FeedService {

    @NotNull
    Feed persistNotification(@NotNull FeedNotification notification);

}
