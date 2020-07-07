package io.outofbox.cronbot.model.job;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class BaseJob {
    @Id
    private String id;
}
