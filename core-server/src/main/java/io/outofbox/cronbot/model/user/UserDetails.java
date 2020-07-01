package io.outofbox.cronbot.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.Set;

@Data
@Validated
@NoArgsConstructor
public class UserDetails {

    public UserDetails(String username){
        this.username = username;
    }

    private String username;
    private String password;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String email;
    private boolean enabled;
    private Set<String> roles = new HashSet<>();
}
