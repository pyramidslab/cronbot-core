package io.outofbox.cronbot.model.user;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
public class BaseUser {
    @Id
    private String id;
}
