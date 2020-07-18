package io.outofbox.cronbot.model.plugin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.outofbox.cronbot.model.MQConfiguration;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("plugin_configuration")
public class PluginConfiguration {
    @JsonProperty("plugin_id")
    private String pluginID;
    private String token;
    private MQConfiguration configuration;
}
