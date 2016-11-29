package com.feedsome.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;

@Data
public class PluginRegistration {

    @NotEmpty
    private final String name;

    @NotNull
    private final Collection<String> categories = new HashSet<>();

}
