package io.outofbox.cronbot.security.util;

import io.outofbox.cronbot.security.config.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.security.auth.login.FailedLoginException;

/**
 *  example @PreAuthorize("@securityHelper.canAccessOwn(#token, #id)")
 * @author ahelmy
 */
@Component("securityHelper")
public class SecurityHelper {
    @Autowired
    private TokenProvider jwtTokenUtil;

    /**
     *
     * @param token
     * @param username
     * @return
     */
    public boolean canAccessOwn(String token, String username){
        token = token.replace(TokenProvider.TOKEN_PREFIX,"");
        try {
            return jwtTokenUtil.isSameUser(token, username);
        } catch (FailedLoginException e) {
            return false;
        }
    }
}
