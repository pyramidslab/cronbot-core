package io.outofbox.cronbot.model.plugin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.outofbox.cronbot.model.MonitoringObj;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("plugins")
public class Plugin extends  BasePlugin {

    private String name;
    private String description;
    @JsonProperty("integration_def")
    private String integrationDefinition;
    private String token;
    private MonitoringObj monitoring;
}
