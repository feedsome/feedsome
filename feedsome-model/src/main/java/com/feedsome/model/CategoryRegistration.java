package com.feedsome.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
@Document(collection = "category_registrations")
@TypeAlias("categoryRegistration")
public class CategoryRegistration {

    @Id
    private String id;

    @NotEmpty
    private String pluginName;

    @DBRef
    @NotNull
    private Category category;

    @NotNull
    private Map<String, Object> metadata = new HashMap<>();

}
