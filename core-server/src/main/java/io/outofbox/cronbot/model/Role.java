package io.outofbox.cronbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author ahelmy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "role")
public class Role{
    @Id
    private Long id;
    private String name;
    private String description;
}