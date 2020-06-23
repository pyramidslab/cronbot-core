package io.outofbox.cronbot.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ahelmy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private String jwt;
}
