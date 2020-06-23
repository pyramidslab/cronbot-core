package io.outofbox.cronbot.security.service;

import io.outofbox.cronbot.security.model.LoginUser;
import io.outofbox.cronbot.security.model.Token;

import javax.security.auth.login.FailedLoginException;

/**
 * @author ahelmy
 */
public interface IAuthenticationService {
    Token authenticate(LoginUser loginUser) throws FailedLoginException;
}
