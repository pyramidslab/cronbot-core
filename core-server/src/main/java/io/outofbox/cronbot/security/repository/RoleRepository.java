package io.outofbox.cronbot.security.repository;

import io.outofbox.cronbot.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author ahelmy
 */
public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
