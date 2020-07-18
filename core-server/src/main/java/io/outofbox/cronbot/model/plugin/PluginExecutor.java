package io.outofbox.cronbot.model.plugin;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PluginExecutor {
    @NotNull
    private String token;
}
