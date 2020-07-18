package io.outofbox.cronbot.service.plugin;

import io.outofbox.cronbot.error.NotFoundException;
import io.outofbox.cronbot.error.OperationFailureException;
import io.outofbox.cronbot.model.plugin.Plugin;
import io.outofbox.cronbot.model.plugin.PluginConfiguration;
import io.outofbox.cronbot.model.plugin.PluginDetails;
import io.outofbox.cronbot.service.common.IGenericCRUDService;

public interface IPluginService extends IGenericCRUDService<Plugin, PluginDetails> {
    PluginConfiguration getPluginConfig(String token) throws OperationFailureException, NotFoundException;
}
