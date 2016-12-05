package com.feedsome.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "categories")
@TypeAlias("category")
public class Category {

    @Id
    private String id;

    @NotEmpty
    @Indexed(unique = true)
    private String name;

}
