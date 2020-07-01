package io.outofbox.cronbot.model.integration;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class IntegrationDefinition {
    @Data
    public static class Definition{
        private String type;
        private boolean required;
        private String description;
        private List<String> enums;
        private String language;
    }

    private String description;
    private Map<String, Definition> definition;
}
