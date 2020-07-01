package io.outofbox.cronbot.model.plugin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.outofbox.cronbot.model.MonitoringObj;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class PluginDetails {

    @NonNull
    private String name;
    private String description;
    @JsonProperty("integration_def")
    @NonNull
    private String integrationDefinition;
}
