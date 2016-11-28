package com.feedsome.service;

import com.feedsome.model.Plugin;
import com.feedsome.repository.PluginRepository;
import org.springframework.validation.annotation.Validated;

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
    public Plugin register(@NotNull final Plugin plugin) {
        final Optional<Plugin> persistedPlugin = pluginRepository.findByName(plugin.getName());
        if(persistedPlugin.isPresent()) {
            // TODO: throw exception
        }



        return pluginRepository.save(plugin);
    }

}