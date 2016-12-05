package com.feedsome.service;

import com.feedsome.model.Feed;
import com.feedsome.service.exception.NotFoundServiceException;

import javax.validation.constraints.NotNull;

/**
 * Defines behaviour for services that handle actions for acquired data {@link Feed} instances
 */
public interface FeedService {

    @NotNull
    Feed persist(@NotNull Feed feedNotification) throws NotFoundServiceException;

}
