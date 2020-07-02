package io.outofbox.cronbot.model.integration;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class BaseIntegration {
    @Id
    private String id;
}
