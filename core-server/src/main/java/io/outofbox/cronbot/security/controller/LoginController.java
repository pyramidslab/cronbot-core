package io.outofbox.cronbot.security.controller;



import io.outofbox.cronbot.security.model.LoginUser;
import io.outofbox.cronbot.security.model.Token;
import io.outofbox.cronbot.security.service.IAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.FailedLoginException;

/**
 *
 * @author ahelmy
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@Slf4j
public class LoginController {

    private IAuthenticationService authenticateService;

    @Autowired
    public LoginController(IAuthenticationService authenticationService){
        this.authenticateService = authenticationService;
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Token> login(@RequestBody LoginUser loginUser) throws AuthenticationException {
        log.info("Login()...");

        try {
            Token token = authenticateService.authenticate(loginUser);
            return ResponseEntity.ok(token);
        } catch (FailedLoginException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}