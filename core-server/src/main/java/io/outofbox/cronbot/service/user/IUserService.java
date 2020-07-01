package io.outofbox.cronbot.service.user;

import io.outofbox.cronbot.error.ConflictExcpetion;
import io.outofbox.cronbot.error.NotFoundException;
import io.outofbox.cronbot.error.OperationFailureException;
import io.outofbox.cronbot.model.user.User;
import io.outofbox.cronbot.model.user.UserDetails;

import java.util.List;

public interface IUserService {
    User findUserByUsername(String username) throws NotFoundException, OperationFailureException;

    List<User> findAllWithPage(int page, int size) throws OperationFailureException;

    User createUser(String username,  UserDetails userDetails) throws OperationFailureException, ConflictExcpetion;

    User updateUser(String username, UserDetails userDetails) throws OperationFailureException, NotFoundException;

    User deleteUser(String username) throws OperationFailureException, NotFoundException;
}
