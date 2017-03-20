package com.feedsome.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;

@Data
@NoArgsConstructor
public class PluginRegistration {

    @NotEmpty
    private String name;

    @NotNull
    private final Collection<String> categories = new HashSet<>();

}
