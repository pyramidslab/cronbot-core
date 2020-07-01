package io.outofbox.cronbot.service.plugin.impl;

import com.mongodb.MongoException;
import io.outofbox.cronbot.error.ConflictExcpetion;
import io.outofbox.cronbot.error.NotFoundException;
import io.outofbox.cronbot.error.OperationFailureException;
import io.outofbox.cronbot.model.MonitoringObj;
import io.outofbox.cronbot.model.plugin.Plugin;
import io.outofbox.cronbot.model.plugin.PluginDetails;
import io.outofbox.cronbot.repository.plugin.PluginRepository;
import io.outofbox.cronbot.service.MonitoringObjHelper;
import io.outofbox.cronbot.service.plugin.IPluginService;
import io.outofbox.cronbot.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PluginService  implements IPluginService {

    private PluginRepository pluginRepository;
    private ObjectUtils objectUtils;
    private MonitoringObjHelper monitoringObjHelper;

    @Autowired
    public PluginService(PluginRepository pluginRepository, ObjectUtils objectUtils, MonitoringObjHelper monitoringObjHelper){
        this.pluginRepository = pluginRepository;
        this.objectUtils = objectUtils;
        this.monitoringObjHelper = monitoringObjHelper;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public Plugin findById(String id) throws NotFoundException, OperationFailureException {
        try {
            Optional<Plugin> plugin = pluginRepository.findById(id);
            if (!plugin.isPresent()) {
                throw new NotFoundException("Plugin not exists");
            }
            return plugin.get();
        }catch (MongoException ex){
            throw new OperationFailureException("Failed to retrieve plugin", ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public List<Plugin> findAllWithPage(int page, int size) throws OperationFailureException {
        try {
            Page<Plugin> plugins = pluginRepository.findAll(PageRequest.of(page,size ));
            return plugins.getContent();
        } catch (MongoException ex) {
            throw new OperationFailureException("Failed to find plugins", ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public Plugin createPlugin(PluginDetails pluginDetails) throws OperationFailureException, ConflictExcpetion {
        try {
            Optional<Plugin> plugin = pluginRepository.findByName(pluginDetails.getName());
            if (plugin.isPresent()) {
                throw new ConflictExcpetion("Plugin name already exists");
            }
            Plugin newPlugin = new Plugin();
            Plugin mergedPlugin = objectUtils.patchObject(newPlugin, pluginDetails);
            MonitoringObj monitoringObj = monitoringObjHelper.createMonitoringObj();
            mergedPlugin.setMonitoring(monitoringObj);
            mergedPlugin = pluginRepository.save(mergedPlugin);
            return mergedPlugin;
        }catch (MongoException ex){
            throw new OperationFailureException("Failed to save plugin", ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public Plugin updatePlugin(String id, PluginDetails pluginDetails) throws OperationFailureException, NotFoundException {
        try {
            Optional<Plugin> plugin = pluginRepository.findById(id);
            if (!plugin.isPresent()) {
                throw new NotFoundException("Plugin not exists");
            }
            Plugin mergedPlugin = objectUtils.patchObject(plugin.get(), pluginDetails);
            MonitoringObj monitoringObj = plugin.get().getMonitoring();
            monitoringObj = monitoringObjHelper.updateMonitoringObj(monitoringObj);
            mergedPlugin.setMonitoring(monitoringObj);
            mergedPlugin = pluginRepository.save(mergedPlugin);
            return mergedPlugin;
        }catch (MongoException ex){
            throw new OperationFailureException("Failed to update plugin", ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public Plugin deletePlugin(String id) throws OperationFailureException, NotFoundException {
        try {
            Optional<Plugin> plugin = pluginRepository.findById(id);
            if (!plugin.isPresent()) {
                throw new NotFoundException("Plugin not exists");
            }
            pluginRepository.deleteById(id);
            return plugin.get();
        }catch (MongoException ex){
            throw new OperationFailureException("Failed to delete plugin", ex);
        }
    }
}
