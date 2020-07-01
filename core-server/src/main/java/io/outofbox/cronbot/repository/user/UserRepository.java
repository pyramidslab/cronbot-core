package io.outofbox.cronbot.repository.user;

import io.outofbox.cronbot.model.user.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, String> {

    Optional<User> findByUsername(String username);
}
