package io.outofbox.cronbot.repository.plugin;

import io.outofbox.cronbot.model.plugin.Plugin;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PluginRepository extends PagingAndSortingRepository<Plugin, String> {
    Optional<Plugin> findByName(String name);
}
