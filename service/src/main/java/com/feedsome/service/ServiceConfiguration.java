package com.feedsome.service;

import com.feedsome.repository.CategoryRepository;
import com.feedsome.repository.FeedRepository;
import com.feedsome.repository.PluginRepository;
import com.feedsome.service.route.configuration.RoutesConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@Import(RoutesConfiguration.class)
@Validated
public class ServiceConfiguration {

    @Bean
    public CategoryService categoryService(@NotNull final CategoryRepository categoryRepository) {
        return new CategoryServiceImpl(categoryRepository);
    }

    @Bean
    public PluginService pluginService(@NotNull final PluginRepository pluginRepository) {
        return new PluginServiceImpl(pluginRepository);
    }

    @Bean
    public FeedService feedService(@NotNull final PluginRepository pluginRepository,
                                   @NotNull final CategoryRepository categoryRepository,
                                   @NotNull final FeedRepository feedRepository) {
        return new FeedServiceImpl(pluginRepository, categoryRepository, feedRepository);
    }

}
