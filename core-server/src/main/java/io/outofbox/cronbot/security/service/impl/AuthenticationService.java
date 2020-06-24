package io.outofbox.cronbot.security.service.impl;

import io.outofbox.cronbot.security.config.TokenProvider;
import io.outofbox.cronbot.security.model.LoginUser;
import io.outofbox.cronbot.security.model.Token;
import io.outofbox.cronbot.security.service.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.FailedLoginException;

/**
 * @author ahelmy
 */
@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Override
    public Token authenticate(LoginUser loginUser) throws FailedLoginException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token;
        token = jwtTokenUtil.generateToken(authentication, loginUser.getUsername());
        return new Token(token);
    }
}
