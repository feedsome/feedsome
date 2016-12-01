package com.feedsome.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;

@Data
public class FeedNotification {

    @NotEmpty
    private String pluginRef;

    @NotEmpty
    private String title;

    @NotEmpty
    private String body;

    @NotEmpty
    private Collection<String> categories = new HashSet<>();

    @NotNull
    private Collection<String> tags = new HashSet<>();

}
