package com.feedsome.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
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


@Document(collection = "feeds")
@TypeAlias("feed")
@Builder
public class Feed {

    @Id
    @Getter
    private String id;

    @DBRef
    @NotEmpty
    @Setter(AccessLevel.NONE)
    @Getter
    private Plugin plugin;

    @NotEmpty
    @Size(min = 5, max = 30)
    @Setter(AccessLevel.NONE)
    @Getter
    private String title;

    @NotEmpty
    @Setter(AccessLevel.NONE)
    @Getter
    private String body;

    @DBRef
    @NotEmpty
    @Getter
    private final Collection<Category> categories = new HashSet<>();

    @NotNull
    @Getter
    private final Collection<String> tags = new HashSet<>();

}
