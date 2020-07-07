package io.outofbox.cronbot.security.service.impl;

import io.outofbox.cronbot.model.user.User;
import io.outofbox.cronbot.security.config.TokenProvider;
import io.outofbox.cronbot.security.model.LoginUser;
import io.outofbox.cronbot.security.model.Token;
import io.outofbox.cronbot.security.service.IAuthenticationService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.security.auth.login.FailedLoginException;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
public class AuthenticationServiceTest {
    @TestConfiguration
    static class AuthenticationServiceTestConfiguration {
        @Bean
        public IAuthenticationService authenticationService() {
            return new AuthenticationService();
        }

        @Bean
        public AuthenticationManager authenticationManager(){
            return new AuthenticationManager() {
                @Override
                public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                    return null;
                }
            };
        }
    }

    @Autowired
    IAuthenticationService authenticationService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private TokenProvider jwtTokenUtil;

    User user;
    String jwt;
    Authentication authentication;

    @Before
    public void setUp() throws FailedLoginException {
        Set<String> roles = new HashSet<>();
        roles.add("Role1");

        user = new User();
        user.setUsername("ahelmy");
        user.setPassword("12345");
        user.setRoles(roles);
        jwt = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.POstGetfAytaZS82wHcjoTyoqhMyxXiWdR7Nn7A29DNSl0EiXLdwJ6xC6AfgZWF1bOsS_TuYI3OG85AmiExREkrS6tDfTQ2B3WXlrr-wp5AokiRbz3_oB4OxG-W9KcEEbDRcZc0nH3L7LzYptiy1PtAylQGxHTWZXtGz4ht0bAecBgmpdgXMguEIcoqPJ1n3pIWk_dUZegpqx0Lka21H6XxUTxiy8OcaarA8zdnPUnV6AmNP3ecFawIFYdvJB_cm-GvpCSbr8G8y_Mllj8f4x9nBH8pQux89_6gUY618iYv7tuPWBFfEbLxtF2pZS6YC1aSfLQxeNe8djT9YjpvRZA";
        authentication =   new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                "12345"
        );

        Mockito.when(jwtTokenUtil.generateToken(authentication, user.getUsername())).thenReturn(jwt);
    }

    @Test
    @DisplayName("authenticationSuccess")
    public void testAuthenticate() throws FailedLoginException {
        Mockito.when(authenticationManager.authenticate(authentication)).thenReturn(authentication);
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername("ahelmy");
        loginUser.setPassword("12345");
        Token token = authenticationService.authenticate(loginUser);
        Assertions.assertThat(token.getJwt()).isEqualTo(jwt);
    }

    @Test(expected = FailedLoginException.class)
    @DisplayName("authenticationWrongPassword")
    public void testAuthenticate_WrongPassword() throws FailedLoginException {
       Mockito.when(authenticationManager.authenticate(authentication)).thenThrow(new BadCredentialsException("Bad credentials"){});

        LoginUser loginUser = new LoginUser();
        loginUser.setUsername("ahelmy");
        loginUser.setPassword("123456");
        Token token = authenticationService.authenticate(loginUser);
        if(token == null || token.getJwt() == null){
            throw new FailedLoginException();
        }
    }
}