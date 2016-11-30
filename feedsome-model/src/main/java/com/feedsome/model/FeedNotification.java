package com.feedsome.model;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;

@Builder
public class FeedNotification {

    @NotEmpty
    @Getter
    private final String pluginRef;

    @NotEmpty
    @Getter
    private final String title;

    @NotEmpty
    @Getter
    private final String body;

    @NotEmpty
    @Getter
    private final Collection<String> categories = new HashSet<>();

    @NotNull
    @Getter
    private final Collection<String> tags = new HashSet<>();

}
