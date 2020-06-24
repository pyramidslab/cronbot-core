package io.outofbox.cronbot.security.model;

import lombok.Data;
import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ahelmy
 */
@Data
@Document(collection = "users")
@Entity
@Table( name="users")
public class LoginUser {
    @Id
    private String id;
    private String username;
    private String password;
    private Set<String> roles = new HashSet<>();
}
