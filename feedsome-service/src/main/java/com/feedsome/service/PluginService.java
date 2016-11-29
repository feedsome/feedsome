package com.feedsome.service;

import com.feedsome.model.Plugin;
import com.feedsome.model.PluginRegistration;

import javax.validation.constraints.NotNull;

/**
 * Defines behaviour for services responsible to handle actions for {@link Plugin} instances
 *
 */
public interface PluginService {

    /**
     * Registers the plugin to the system and returns a workable instance.
     * @param plugin the {@link Plugin} instance to be registered
     * @return the newly registered plugin
     */
    @NotNull
    Plugin register(@NotNull PluginRegistration plugin);


}
