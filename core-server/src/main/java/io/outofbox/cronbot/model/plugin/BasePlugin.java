package io.outofbox.cronbot.model.plugin;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class BasePlugin {
    @Id
    private String id;
}
