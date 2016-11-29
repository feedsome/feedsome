package com.feedsome.service;

import com.feedsome.model.Feed;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.beans.FeatureDescriptor;

@Validated
public class RedisNotificationService implements NotificationService {


    @Override
    @NotNull
    public Feed push(@NotNull final Feed feed) {
        return null;
    }
}
