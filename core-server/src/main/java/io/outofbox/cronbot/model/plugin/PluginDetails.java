package io.outofbox.cronbot.model.plugin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.outofbox.cronbot.model.integration.IntegrationDefinition;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Data
@Validated
public class PluginDetails {

    private String name;
    private String description;
    @JsonProperty("integration_def")
    private IntegrationDefinition integrationDefinition;
}
