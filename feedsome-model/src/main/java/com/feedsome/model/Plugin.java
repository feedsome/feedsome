package com.feedsome.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.TreeSet;

@Data
@Document(collection = "plugins")
@TypeAlias("plugin")
public class Plugin {
    
    @Id
    private String id;

    @NotEmpty
    @Indexed(unique = true)
    private String name;

    @NotNull
    private Collection<String> categoryNames = new TreeSet<>();

    private boolean active;

}
