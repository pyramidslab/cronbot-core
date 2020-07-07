package io.outofbox.cronbot.model.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document("integrations")
public class Integration extends BaseIntegration {
    private String name;
    private String description;
    @JsonProperty("plugin_definition_values")
    private Map<String, Object> pluginDefValues;
    @JsonProperty("plugin_id")
    private String pluginId;

}
