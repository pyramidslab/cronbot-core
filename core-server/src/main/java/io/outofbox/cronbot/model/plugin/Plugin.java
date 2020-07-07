package io.outofbox.cronbot.model.plugin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.outofbox.cronbot.model.MonitoringObj;
import io.outofbox.cronbot.model.integration.IntegrationDefinition;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document("plugins")
public class Plugin extends  BasePlugin {

    private String name;
    private String description;
    @JsonProperty("integration_def")
    private IntegrationDefinition integrationDefinition;
    private String token;
    private MonitoringObj monitoring;
}
