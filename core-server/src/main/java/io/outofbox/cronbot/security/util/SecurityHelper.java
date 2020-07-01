package io.outofbox.cronbot.security.util;

import io.outofbox.cronbot.security.config.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.security.auth.login.FailedLoginException;

/**
 *  example @PreAuthorize("@securityHelper.canAccessOwn( #id)")
 * @author ahelmy
 */
@Component("securityHelper")
public class SecurityHelper {

    /**
     *
     * @param username
     * @return
     */
    public boolean canAccessOwn(String username){
        return username.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
