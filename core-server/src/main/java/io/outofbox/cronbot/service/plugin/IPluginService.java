package io.outofbox.cronbot.service.plugin;

import io.outofbox.cronbot.error.ConflictExcpetion;
import io.outofbox.cronbot.error.NotFoundException;
import io.outofbox.cronbot.error.OperationFailureException;
import io.outofbox.cronbot.model.plugin.Plugin;
import io.outofbox.cronbot.model.plugin.PluginDetails;
import io.outofbox.cronbot.model.user.User;

import java.util.List;

public interface IPluginService {

    Plugin findById(String id) throws NotFoundException, OperationFailureException;

    List<Plugin> findAllWithPage(int page, int size) throws OperationFailureException;

    Plugin createPlugin(PluginDetails pluginDetails) throws OperationFailureException, ConflictExcpetion;

    Plugin updatePlugin(String id, PluginDetails pluginDetails) throws OperationFailureException, NotFoundException;

    Plugin deletePlugin(String id) throws OperationFailureException,NotFoundException;
}
