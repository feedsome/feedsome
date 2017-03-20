package com.feedsome.model;

import lombok.AccessLevel;
import lombok.Data;
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

@Data
@Document(collection = "feeds")
@TypeAlias("feed")
public class Feed {

    @Id
    private String id;

    @DBRef
    @NotEmpty
    private Plugin plugin;

    @NotEmpty
    @Size(min = 5, max = 30)
    private String title;

    @NotEmpty
    private String body;

    @DBRef
    @NotEmpty
    @Setter(AccessLevel.PACKAGE)
    private final Collection<Category> categories = new HashSet<>();

    @NotNull
    @Setter(AccessLevel.PACKAGE)
    private final Collection<String> tags = new HashSet<>();

}
