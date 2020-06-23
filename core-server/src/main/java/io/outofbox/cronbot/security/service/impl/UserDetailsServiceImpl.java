package io.outofbox.cronbot.security.service.impl;

import io.outofbox.cronbot.model.user.User;
import io.outofbox.cronbot.repository.user.UserRepository;
import io.outofbox.cronbot.security.model.LoginUser;
import io.outofbox.cronbot.security.repository.LoginUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ahelmy
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private LoginUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<LoginUser> loginUser = userRepository.findByUsername(username);
        if(!loginUser.isPresent()){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(loginUser.get().getUsername(), loginUser.get().getPassword(), getAuthority(loginUser.get()));
    }

    private Set<SimpleGrantedAuthority> getAuthority(LoginUser user) {
        return user.getRoles().stream().map(role->new SimpleGrantedAuthority("ROLE_"+role)).collect(Collectors.toSet());
    }
}
