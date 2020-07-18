package io.outofbox.cronbot.repository.plugin;

import io.outofbox.cronbot.model.plugin.PluginConfiguration;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PluginConfigurationRepository extends CrudRepository<PluginConfiguration, String> {
    Optional<PluginConfiguration> findByPluginIDAndToken(String id, String token);
    Optional<PluginConfiguration> findByToken(String token);
}
