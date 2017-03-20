package com.feedsome.service;

import com.feedsome.model.Plugin;
import com.feedsome.model.PluginRegistration;
import com.feedsome.repository.PluginRepository;
import com.feedsome.service.exception.DuplicateServiceException;
import com.feedsome.service.exception.NotFoundServiceException;
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
    public Plugin register(@NotNull @Valid final PluginRegistration pluginRegistration) throws DuplicateServiceException {
        final Optional<Plugin> persistedPlugin = pluginRepository.findByName(pluginRegistration.getName());
        if(persistedPlugin.isPresent()) {
            throw new DuplicateServiceException();
        }

        final Plugin plugin = new Plugin();
        plugin.setCategoryNames(pluginRegistration.getCategories());
        plugin.setName(pluginRegistration.getName());

        plugin.setActive(true);

        return pluginRepository.save(plugin);
    }

    @Override
    @NotNull
    public Plugin disableByName(@NotEmpty final String pluginName) throws NotFoundServiceException {

        final Optional<Plugin> persistedPlugin = pluginRepository.findByName(pluginName);
        if(!persistedPlugin.isPresent()) {
            throw new NotFoundServiceException();
        }

        final Plugin actualPlugin = persistedPlugin.get();
        actualPlugin.setActive(false);

        return pluginRepository.save(actualPlugin);
    }

    @Override
    @NotNull
    public Plugin enableByName(@NotEmpty final String pluginName) throws NotFoundServiceException {
        //TODO: custom query with mongo template for this one!
        final Optional<Plugin> persistedPlugin = pluginRepository.findByName(pluginName);
        if(!persistedPlugin.isPresent()) {
            throw new NotFoundServiceException();
        }

        final Plugin actualPlugin = persistedPlugin.get();
        actualPlugin.setActive(true);

        return pluginRepository.save(actualPlugin);
    }

}