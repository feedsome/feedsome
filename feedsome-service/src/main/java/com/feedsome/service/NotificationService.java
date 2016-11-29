package com.feedsome.service;

import com.feedsome.model.Feed;

import javax.validation.constraints.NotNull;

public interface NotificationService {

    @NotNull
    Feed push(@NotNull Feed feed);

}
