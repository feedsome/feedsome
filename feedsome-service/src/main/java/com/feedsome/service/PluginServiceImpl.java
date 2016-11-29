package com.feedsome.service;

import com.feedsome.model.Plugin;
import com.feedsome.model.PluginRegistration;
import com.feedsome.repository.PluginRepository;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Validated
public class PluginServiceImpl implements PluginService {

    private final PluginRepository pluginRepository;

    public PluginServiceImpl(@NotNull final PluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
    }

    @Override
    @NotNull
    public Plugin register(@NotNull @Valid final PluginRegistration pluginRegistration) {
        final Optional<Plugin> persistedPlugin = pluginRepository.findByName(pluginRegistration.getName());
        if(persistedPlugin.isPresent()) {

            // TODO: throw exception
        }

        final Plugin plugin = new Plugin();
        plugin.setCategoryNames(pluginRegistration.getCategories());
        plugin.setName(pluginRegistration.getName());

        plugin.setActive(true);

        return pluginRepository.save(plugin);
    }

    public Plugin disableByName(@NotEmpty final String pluginName) {

        final Optional<Plugin> persistedPlugin = pluginRepository.findByName(pluginName);
        if(!persistedPlugin.isPresent()) {
            //TODO: throw exception if not found
        }

        final Plugin actualPlugin = persistedPlugin.get();
        actualPlugin.setActive(false);

        return pluginRepository.save(actualPlugin);
    }

    //TODO: custom query with mongo template for this one!
    public Plugin enableByName(@NotEmpty final String pluginName) {
        final Optional<Plugin> persistedPlugin = pluginRepository.findByName(pluginName);
        if(!persistedPlugin.isPresent()) {
            //TODO: throw exception if not found
        }

        final Plugin actualPlugin = persistedPlugin.get();
        actualPlugin.setActive(true);

        return pluginRepository.save(actualPlugin);
    }

}