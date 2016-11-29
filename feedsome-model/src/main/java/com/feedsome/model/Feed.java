package com.feedsome.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;


@Builder
@Document(collection = "feeds")
@TypeAlias("feed")
public class Feed {

    @Id
    private String id;

    @DBRef
    @NotEmpty
    @Setter(AccessLevel.NONE)
    private final Plugin pluginReference;

    @NotEmpty
    @Size(min = 5, max = 30)
    @Setter(AccessLevel.NONE)
    private final String title;

    @NotEmpty
    @Size(min = 20, max = 100)
    @Setter(AccessLevel.NONE)
    private final String description;

    @NotEmpty
    @Setter(AccessLevel.NONE)
    private final String body;

    @NotNull
    private final Collection<String> tags = new HashSet<>();

}
