package io.outofbox.cronbot.service.plugin;

import io.outofbox.cronbot.model.plugin.PluginConfiguration;

public interface IPluginConfigurationService {
    PluginConfiguration create(String pluginId);
}
