package io.outofbox.cronbot.model.user;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class BaseUser {
    @Id
    private String id;
}
