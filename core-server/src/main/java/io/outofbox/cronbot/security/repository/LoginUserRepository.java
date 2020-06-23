package io.outofbox.cronbot.security.repository;

import io.outofbox.cronbot.model.user.User;
import io.outofbox.cronbot.security.model.LoginUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LoginUserRepository extends CrudRepository<LoginUser, Long> {

    Optional<LoginUser> findByUsername(String username);
}
